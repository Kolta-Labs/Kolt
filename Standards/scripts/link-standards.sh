#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# link-standards.sh — Symlink Kolt standards & AI docs into configured projects
#
# USAGE:
#   ./scripts/link-standards.sh                  Interactive menu (default)
#   ./scripts/link-standards.sh --all            Link/relink all projects in data file
#   ./scripts/link-standards.sh --list           List configured projects & status
#   ./scripts/link-standards.sh --update         Scan Tech folder & update projects.txt
#   ./scripts/link-standards.sh --select         Select projects to link by number
#   ./scripts/link-standards.sh --file <path>    Use custom data file
#   ./scripts/link-standards.sh <type> <path>    Link a specific project (type: kmp|android)
#   ./scripts/link-standards.sh --help           Show this message
# ─────────────────────────────────────────────────────────────────────────────
set -eo pipefail

trap 'echo -e "\n\033[0;36mℹ  Exiting.\033[0m"; exit 0' INT

# ── Color helpers ────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; BLUE='\033[0;34m'; BOLD='\033[1m'; NC='\033[0m'
info()    { echo -e "${CYAN}ℹ${NC}  $*"; }
success() { echo -e "${GREEN}✓${NC}  $*"; }
warn()    { echo -e "${YELLOW}⚠${NC}  $*"; }
err()     { echo -e "${RED}✗  $*${NC}" >&2; }

# ── Directories ──────────────────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Resolve STANDARDS_DIR (directory containing steering/)
if [[ -d "${SCRIPT_DIR}/../steering" ]]; then
    STANDARDS_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
elif [[ -d "${SCRIPT_DIR}/../Standards/steering" ]]; then
    STANDARDS_DIR="$(cd "${SCRIPT_DIR}/../Standards" && pwd)"
else
    STANDARDS_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
fi

# Resolve TECH_DIR (root containing projects)
find_tech_dir() {
    local dir="$SCRIPT_DIR"
    while [[ "$dir" != "/" ]]; do
        if [[ "$(basename "$dir")" == "Tech" ]]; then
            echo "$dir"
            return 0
        fi
        dir="$(dirname "$dir")"
    done
    (cd "${SCRIPT_DIR}/../../../../.." 2>/dev/null && pwd) || echo ""
}
TECH_DIR="${TECH_DIR:-$(find_tech_dir)}"
DATA_FILE="${DATA_FILE:-${SCRIPT_DIR}/projects.txt}"
PROJECTS=()

# ── Type detection helper ────────────────────────────────────────────────────
detect_project_type() {
    local dir="$1"

    # Exclude IntelliJ plugins or build-logic
    if [[ "$(basename "$dir")" == "intellij-plugin" || "$(basename "$dir")" == "build-logic" ]]; then
        echo "unknown"
        return
    fi

    # 1. Look for commonMain or iosMain source sets
    if find "$dir" -maxdepth 5 -type d \( -name "commonMain" -o -name "iosMain" \) 2>/dev/null | grep -q .; then
        echo "kmp"
        return
    fi

    # 2. Look for multiplatform plugin in gradle files
    if grep -rnE "kotlin\([\"']multiplatform[\"']\)|org\.jetbrains\.kotlin\.multiplatform" "$dir" --include="*.gradle*" --exclude-dir=".gradle" --exclude-dir="build" 2>/dev/null | grep -q .; then
        echo "kmp"
        return
    fi

    # 3. Path heuristic: under KMP/
    local rel="${dir#$TECH_DIR/}"
    if [[ "$rel" == KMP/* ]]; then
        echo "kmp"
        return
    fi

    # 4. Check for Android application/library plugins or AndroidManifest
    if grep -rnE "com\.android\.application|com\.android\.library" "$dir" --include="*.gradle*" --exclude-dir=".gradle" --exclude-dir="build" 2>/dev/null | grep -q .; then
        echo "android"
        return
    fi

    # 5. Path heuristic: under Android/
    if [[ "$rel" == Android/* ]]; then
        echo "android"
        return
    fi

    echo "unknown"
}

# ── Load projects from data file ─────────────────────────────────────────────
load_projects() {
    if [[ ! -f "$DATA_FILE" ]]; then
        PROJECTS=()
        return 0
    fi

    PROJECTS=()
    while IFS= read -r line || [[ -n "$line" ]]; do
        line="$(echo "$line" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
        [[ -z "$line" || "$line" =~ ^# ]] && continue

        local type path
        type="$(echo "$line" | awk '{print $1}')"
        path="$(echo "$line" | awk '{$1=""; print $0}' | sed -e 's/^[[:space:]]*//')"

        if [[ "$path" != /* ]]; then
            path="${TECH_DIR}/${path}"
        fi

        PROJECTS+=("${type}|${path}")
    done < "$DATA_FILE"
}

# ── Link a single project ────────────────────────────────────────────────────
link_project() {
    local type="$1"
    local target="$2"

    case "$type" in
        kmp|android) ;;
        *) err "Invalid type '${type}'. Must be 'kmp' or 'android'."; return 1 ;;
    esac

    local steering_dir="${STANDARDS_DIR}/steering/${type}"
    if [[ ! -d "$steering_dir" ]]; then
        err "Steering directory not found: ${steering_dir}"
        return 1
    fi

    if [[ ! -d "$target" ]]; then
        warn "Target directory does not exist: ${target} (skipping)"
        return 1
    fi

    # Remove existing symlinks or broken references
    rm -f "${target}/.standards" "${target}/AGENTS.md" "${target}/CLAUDE.md" "${target}/GEMINI.md"

    # Soft-link the standards steering folder and AI docs
    ln -sfn "${steering_dir}" "${target}/.standards"
    ln -sfn .standards/AGENTS.md "${target}/AGENTS.md"
    ln -sfn .standards/AGENTS.md "${target}/CLAUDE.md"
    ln -sfn .standards/AGENTS.md "${target}/GEMINI.md"

    local display_path="$target"
    if [[ "$target" == "$TECH_DIR"/* ]]; then
        display_path="${target#$TECH_DIR/}"
    fi

    success "Linked [${type}] -> ${display_path}"
}

# ── Option 1: Update project list in Tech folder ─────────────────────────────
update_project_list() {
    if [[ ! -d "$TECH_DIR" ]]; then
        err "Tech directory not found: ${TECH_DIR}"
        return 1
    fi

    info "Scanning Tech directory for projects: ${TECH_DIR}..."

    local candidates=()

    # 1. Root & subprojects with settings.gradle / settings.gradle.kts or existing .standards
    while IFS= read -r dir; do
        [[ -n "$dir" ]] && candidates+=("$dir")
    done < <(find "$TECH_DIR" -maxdepth 6 \( -name "settings.gradle" -o -name "settings.gradle.kts" -o -name ".standards" \) \
        -not -path "*/.claude/*" \
        -not -path "*/.git/*" \
        -not -path "*/build/*" \
        -not -path "*/build-logic/*" \
        -not -path "*/project-templates/*" \
        -not -path "*/node_modules/*" \
        -not -path "*/.gradle/*" \
        -exec dirname {} \; 2>/dev/null | sort -u)

    # 2. Preserve any existing valid paths from current DATA_FILE
    if [[ -f "$DATA_FILE" ]]; then
        while IFS= read -r line || [[ -n "$line" ]]; do
            line="$(echo "$line" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
            [[ -z "$line" || "$line" =~ ^# ]] && continue
            local path
            path="$(echo "$line" | awk '{$1=""; print $0}' | sed -e 's/^[[:space:]]*//')"
            [[ "$path" != /* ]] && path="${TECH_DIR}/${path}"
            [[ -d "$path" ]] && candidates+=("$path")
        done < "$DATA_FILE"
    fi

    # Deduplicate candidate directories
    local unique_candidates=()
    while IFS= read -r dir; do
        [[ -n "$dir" ]] && unique_candidates+=("$dir")
    done < <(printf "%s\n" "${candidates[@]}" | sort -u)

    local android_projects=()
    local kmp_projects=()
    local kolt_projects=()

    for dir in "${unique_candidates[@]}"; do
        local type
        type="$(detect_project_type "$dir")"
        if [[ "$type" != "kmp" && "$type" != "android" ]]; then
            continue
        fi

        local rel="${dir#$TECH_DIR/}"

        if [[ "$rel" == KMP/Kolta/KoltLibs* ]]; then
            kolt_projects+=("${type}|${rel}")
        elif [[ "$type" == "android" ]]; then
            android_projects+=("${type}|${rel}")
        else
            kmp_projects+=("${type}|${rel}")
        fi
    done

    # Write out updated DATA_FILE
    {
        echo "# ─────────────────────────────────────────────────────────────────────────────"
        echo "# projects.txt — Configuration file for Kolt standards linking"
        echo "#"
        echo "# FORMAT:"
        echo "#   <type>  <project_path>"
        echo "#"
        echo "#   - <type>: 'android' or 'kmp'"
        echo "#   - <project_path>: Relative to Tech root (or absolute path)"
        echo "#   - Lines beginning with '#' or empty lines are ignored."
        echo "# ─────────────────────────────────────────────────────────────────────────────"
        echo ""
        echo "# Android Projects"
        for p in "${android_projects[@]}"; do
            local t="${p%%|*}"
            local r="${p#*|}"
            printf "%-10s %s\n" "$t" "$r"
        done
        echo ""
        echo "# KMP Projects"
        for p in "${kmp_projects[@]}"; do
            local t="${p%%|*}"
            local r="${p#*|}"
            printf "%-10s %s\n" "$t" "$r"
        done
        echo ""
        echo "# Kolt Repository & Subprojects"
        for p in "${kolt_projects[@]}"; do
            local t="${p%%|*}"
            local r="${p#*|}"
            printf "%-10s %s\n" "$t" "$r"
        done
    } > "$DATA_FILE"

    local total_count=$((${#android_projects[@]} + ${#kmp_projects[@]} + ${#kolt_projects[@]}))
    success "Updated ${DATA_FILE}"
    info "Found ${total_count} projects: ${#android_projects[@]} Android, ${#kmp_projects[@]} KMP, ${#kolt_projects[@]} Kolt subprojects."
}

# ── Option 2: List projects with type and status ─────────────────────────────
list_projects() {
    load_projects
    if [[ ${#PROJECTS[@]} -eq 0 ]]; then
        warn "No projects found in ${DATA_FILE}. Run option 1 to scan and populate projects."
        return
    fi

    echo -e "${BLUE}Configured projects from ${DATA_FILE}:${NC}\n"
    local total=0
    local linked_count=0
    local broken_count=0
    local unlinked_count=0
    local missing_count=0

    local idx=1
    for item in "${PROJECTS[@]}"; do
        local type="${item%%|*}"
        local path="${item#*|}"
        local display_path="$path"
        if [[ "$path" == "$TECH_DIR"/* ]]; then
            display_path="${path#$TECH_DIR/}"
        fi

        local status=""
        if [[ ! -d "$path" ]]; then
            status="${RED}missing directory${NC}"
            ((missing_count++))
        elif [[ -L "${path}/.standards" ]]; then
            local dest
            dest="$(readlink "${path}/.standards" || echo "unknown")"
            if [[ -e "${path}/.standards" ]]; then
                status="${GREEN}linked -> ${dest}${NC}"
                ((linked_count++))
            else
                status="${RED}broken link -> ${dest}${NC}"
                ((broken_count++))
            fi
        else
            status="${YELLOW}not linked${NC}"
            ((unlinked_count++))
        fi
        ((total++))

        local type_color="$CYAN"
        [[ "$type" == "android" ]] && type_color="$GREEN"

        printf "  %2d) %b%-9s%b %-45s [%b]\n" "$idx" "$type_color" "[$type]" "$NC" "$display_path" "$status"
        ((idx++))
    done

    echo ""
    echo -e "${CYAN}Summary:${NC} ${total} total | ${GREEN}${linked_count} linked${NC} | ${YELLOW}${unlinked_count} not linked${NC} | ${RED}${broken_count} broken${NC} | ${RED}${missing_count} missing${NC}"
}

# ── Option 3: Link standards to all projects ─────────────────────────────────
link_all() {
    load_projects
    if [[ ${#PROJECTS[@]} -eq 0 ]]; then
        warn "No projects found in ${DATA_FILE}. Run option 1 first."
        return
    fi

    info "Starting Kolt standards link run for all configured projects..."
    info "Standards source: ${STANDARDS_DIR}"
    info "Data file:        ${DATA_FILE}"
    echo ""

    local success_count=0
    local fail_count=0

    for item in "${PROJECTS[@]}"; do
        local type="${item%%|*}"
        local path="${item#*|}"

        if link_project "$type" "$path"; then
            ((success_count++))
        else
            ((fail_count++))
        fi
    done

    echo ""
    info "Link run complete: ${success_count} succeeded, ${fail_count} failed/skipped."
}

# ── Option 4: Link standards to selected projects ────────────────────────────
link_selected() {
    load_projects
    if [[ ${#PROJECTS[@]} -eq 0 ]]; then
        warn "No projects found in ${DATA_FILE}. Run option 1 to scan and populate projects."
        return
    fi

    echo -e "${BLUE}Configured projects:${NC}\n"
    local idx=1
    for item in "${PROJECTS[@]}"; do
        local type="${item%%|*}"
        local path="${item#*|}"
        local display_path="$path"
        if [[ "$path" == "$TECH_DIR"/* ]]; then
            display_path="${path#$TECH_DIR/}"
        fi

        local status=""
        if [[ ! -d "$path" ]]; then
            status="${RED}missing directory${NC}"
        elif [[ -L "${path}/.standards" && -e "${path}/.standards" ]]; then
            status="${GREEN}linked${NC}"
        elif [[ -L "${path}/.standards" ]]; then
            status="${RED}broken link${NC}"
        else
            status="${YELLOW}not linked${NC}"
        fi

        local type_color="$CYAN"
        [[ "$type" == "android" ]] && type_color="$GREEN"

        printf "  %2d) %b%-9s%b %-45s [%b]\n" "$idx" "$type_color" "[$type]" "$NC" "$display_path" "$status"
        ((idx++))
    done

    echo ""
    read -rp "Enter project numbers to link (comma-separated, e.g. 1, 3, 5-7, or 'all', 'q' to cancel): " selection_input

    selection_input="$(echo "$selection_input" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"

    if [[ -z "$selection_input" || "$selection_input" =~ ^[qQ]$ || "$selection_input" == "cancel" ]]; then
        info "Selection cancelled."
        return
    fi

    local selected_indices=()
    if [[ "$selection_input" == "all" ]]; then
        for ((i=1; i<=${#PROJECTS[@]}; i++)); do
            selected_indices+=("$i")
        done
    else
        IFS=',' read -ra parts <<< "$selection_input"
        for part in "${parts[@]}"; do
            part="$(echo "$part" | tr -d '[:space:]')"
            if [[ "$part" =~ ^([0-9]+)-([0-9]+)$ ]]; then
                local start="${BASH_REMATCH[1]}"
                local end="${BASH_REMATCH[2]}"
                if (( start <= end )); then
                    for ((i=start; i<=end; i++)); do
                        if (( i >= 1 && i <= ${#PROJECTS[@]} )); then
                            selected_indices+=("$i")
                        else
                            warn "Index $i out of range (1-${#PROJECTS[@]}), skipping."
                        fi
                    done
                fi
            elif [[ "$part" =~ ^[0-9]+$ ]]; then
                local num="$part"
                if (( num >= 1 && num <= ${#PROJECTS[@]} )); then
                    selected_indices+=("$num")
                else
                    warn "Index $num out of range (1-${#PROJECTS[@]}), skipping."
                fi
            elif [[ -n "$part" ]]; then
                warn "Invalid entry '$part' ignored."
            fi
        done
    fi

    local unique_indices=()
    while IFS= read -r i; do
        [[ -n "$i" ]] && unique_indices+=("$i")
    done < <(printf "%s\n" "${selected_indices[@]}" 2>/dev/null | sort -nu)

    if [[ ${#unique_indices[@]} -eq 0 ]]; then
        warn "No valid projects selected."
        return
    fi

    echo ""
    info "Linking standards to ${#unique_indices[@]} selected project(s)..."
    echo ""

    local success_count=0
    local fail_count=0

    for i in "${unique_indices[@]}"; do
        local item="${PROJECTS[$((i-1))]}"
        local type="${item%%|*}"
        local path="${item#*|}"

        if link_project "$type" "$path"; then
            ((success_count++))
        else
            ((fail_count++))
        fi
    done

    echo ""
    info "Selected link run complete: ${success_count} succeeded, ${fail_count} failed/skipped."
}

# ── Interactive Menu ─────────────────────────────────────────────────────────
interactive_menu() {
    while true; do
        echo ""
        echo -e "${BLUE}================================================================${NC}"
        echo -e "${BLUE}                    Kolt Standards Linker                       ${NC}"
        echo -e "${BLUE}================================================================${NC}"
        echo -e "  Tech Root:        ${CYAN}${TECH_DIR}${NC}"
        echo -e "  Standards Source: ${CYAN}${STANDARDS_DIR}${NC}"
        echo -e "  Data File:        ${CYAN}${DATA_FILE}${NC}"
        echo ""
        echo -e "  ${GREEN}1)${NC} Update the project lists in Tech folder with proper type (kmp/android)"
        echo -e "  ${GREEN}2)${NC} List the projects with its type and status"
        echo -e "  ${GREEN}3)${NC} Link standards to all projects"
        echo -e "  ${GREEN}4)${NC} Link standards to selected projects with project numbers"
        echo -e "  ${GREEN}5)${NC} Exit"
        echo ""
        read -rp "Select an option [1-5]: " choice
        echo ""

        case "$choice" in
            1)
                update_project_list
                ;;
            2)
                list_projects
                ;;
            3)
                link_all
                ;;
            4)
                link_selected
                ;;
            5|[qQ]|exit|quit)
                info "Goodbye!"
                break
                ;;
            *)
                warn "Invalid selection '$choice'. Please choose 1, 2, 3, 4, or 5."
                ;;
        esac

        echo ""
        read -rp "Press Enter to return to menu..." _unused
    done
}

# ── Help / Usage ─────────────────────────────────────────────────────────────
show_help() {
    sed -n '2,13p' "$0" | sed 's/^# //' | sed 's/^#//'
    echo ""
    echo "INTERACTIVE OPTIONS:"
    echo "  1) Update project list in Tech root with proper type (kmp/android)"
    echo "  2) List configured projects with type and link status"
    echo "  3) Link standards to all projects"
    echo "  4) Link standards to selected projects with project numbers"
}

# ── Entry Point ──────────────────────────────────────────────────────────────
if [[ $# -eq 0 ]]; then
    interactive_menu
elif [[ "${1:-}" == "--interactive" ]] || [[ "${1:-}" == "-i" ]]; then
    interactive_menu
elif [[ "${1:-}" == "--update" ]] || [[ "${1:-}" == "-u" ]]; then
    update_project_list
elif [[ "${1:-}" == "--all" ]]; then
    link_all
elif [[ "${1:-}" == "--list" ]] || [[ "${1:-}" == "-l" ]]; then
    list_projects
elif [[ "${1:-}" == "--select" ]] || [[ "${1:-}" == "-s" ]]; then
    link_selected
elif [[ "${1:-}" == "--file" ]] && [[ $# -ge 2 ]]; then
    DATA_FILE="$2"
    shift 2
    if [[ $# -eq 0 ]]; then
        interactive_menu
    elif [[ "${1:-}" == "--all" ]]; then
        link_all
    elif [[ "${1:-}" == "--list" ]]; then
        list_projects
    elif [[ "${1:-}" == "--update" ]]; then
        update_project_list
    elif [[ "${1:-}" == "--select" ]]; then
        link_selected
    fi
elif [[ "${1:-}" == "--help" ]] || [[ "${1:-}" == "-h" ]]; then
    show_help
elif [[ $# -eq 2 ]]; then
    link_project "$1" "$2"
else
    err "Invalid arguments. Run with --help for usage."
    exit 1
fi
