#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# link-standards.sh — Symlink Kolt standards & AI docs into configured projects
#
# USAGE:
#   ./scripts/link-standards.sh                  Link/relink all projects in data file
#   ./scripts/link-standards.sh --all            Same as above
#   ./scripts/link-standards.sh --list           List configured projects & status
#   ./scripts/link-standards.sh --file <path>    Use custom data file
#   ./scripts/link-standards.sh <type> <path>    Link a specific project (type: kmp|android)
#   ./scripts/link-standards.sh --help           Show this message
# ─────────────────────────────────────────────────────────────────────────────
set -euo pipefail

# ── Directories ──────────────────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
STANDARDS_DIR="${REPO_ROOT}/Standards"
TECH_DIR="${TECH_DIR:-$(cd "${REPO_ROOT}/../../.." && pwd)}"
DATA_FILE="${SCRIPT_DIR}/projects.txt"

# ── Color helpers ────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; BLUE='\033[0;34m'; NC='\033[0m'
info()    { echo -e "${CYAN}ℹ${NC}  $*"; }
success() { echo -e "${GREEN}✓${NC}  $*"; }
warn()    { echo -e "${YELLOW}⚠${NC}  $*"; }
err()     { echo -e "${RED}✗  $*${NC}" >&2; }

# ── Load projects from data file ─────────────────────────────────────────────
load_projects() {
    if [[ ! -f "$DATA_FILE" ]]; then
        err "Data file not found: ${DATA_FILE}"
        exit 1
    fi

    PROJECTS=()
    while IFS= read -r line || [[ -n "$line" ]]; do
        # Trim leading/trailing whitespace
        line="$(echo "$line" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"

        # Skip comments and empty lines
        [[ -z "$line" || "$line" =~ ^# ]] && continue

        # Parse type and path (supports whitespace or tab separated)
        local type path
        type="$(echo "$line" | awk '{print $1}')"
        path="$(echo "$line" | awk '{$1=""; print $0}' | sed -e 's/^[[:space:]]*//')"

        # Resolve relative path against TECH_DIR
        if [[ "$path" != /* ]]; then
            path="${TECH_DIR}/${path}"
        fi

        PROJECTS+=("${type}|${path}")
    done < "$DATA_FILE"
}

# ── Function: Link a single project ──────────────────────────────────────────
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

    success "Linked [${type}] -> ${target}"
}

# ── Function: List projects & status ─────────────────────────────────────────
list_projects() {
    load_projects
    echo -e "${BLUE}Configured projects from ${DATA_FILE}:${NC}\n"
    for item in "${PROJECTS[@]}"; do
        local type="${item%%|*}"
        local path="${item#*|}"
        local status=""

        if [[ ! -d "$path" ]]; then
            status="${RED}missing directory${NC}"
        elif [[ -L "${path}/.standards" ]]; then
            local dest
            dest="$(readlink "${path}/.standards" || echo "unknown")"
            if [[ -e "${path}/.standards" ]]; then
                status="${GREEN}linked -> ${dest}${NC}"
            else
                status="${RED}broken link -> ${dest}${NC}"
            fi
        else
            status="${YELLOW}not linked${NC}"
        fi

        printf "  %-7s  %-50s  [%b]\n" "[$type]" "$path" "$status"
    done
}

# ── Function: Link all configured projects ───────────────────────────────────
link_all() {
    load_projects
    info "Starting Kolt standards link run..."
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

# ── Entry Point ──────────────────────────────────────────────────────────────
if [[ $# -eq 0 ]] || [[ "${1:-}" == "--all" ]]; then
    link_all
elif [[ "${1:-}" == "--list" ]]; then
    list_projects
elif [[ "${1:-}" == "--file" ]] && [[ $# -ge 2 ]]; then
    DATA_FILE="$2"
    shift 2
    if [[ $# -eq 0 ]] || [[ "${1:-}" == "--all" ]]; then
        link_all
    elif [[ "${1:-}" == "--list" ]]; then
        list_projects
    fi
elif [[ "${1:-}" == "--help" ]] || [[ "${1:-}" == "-h" ]]; then
    sed -n '2,12p' "$0" | sed 's/^# //' | sed 's/^#//'
elif [[ $# -eq 2 ]]; then
    link_project "$1" "$2"
else
    err "Invalid arguments. Run with --help for usage."
    exit 1
fi
