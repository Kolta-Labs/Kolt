// Component catalog — mirrors the showcase's 24 cards 1:1. `demoAnchor` is the
// id injected into showcase/index.html so the embedded iframe can scroll to it.
export const COMPONENTS = [
    // ---- Core ----
    {
        id: 'buttons-actions', category: 'core', demoAnchor: 'demo-buttons-actions',
        title: 'Buttons & Actions',
        description: 'Themed button states supporting primary, secondary, icon, image, iOS-style, and text/link alignments.',
        composables: [
            { name: 'KoltButton', desc: 'Standard primary filled button', path: 'libs/compose-kmp/.../components/core/buttons/KoltButton.kt' },
            { name: 'KoltTonalButton', desc: 'Filled tonal button using secondary container colors', path: 'libs/compose-kmp/.../components/core/buttons/KoltTonalButton.kt' },
            { name: 'KoltOutlinedButton', desc: 'Outlined button using outline borders', path: 'libs/compose-kmp/.../components/core/buttons/KoltOutlinedButton.kt' },
            { name: 'KoltIosButton', desc: 'iOS-style button with opacity-fade feedback (filled/tinted/gray/plain)', path: 'libs/compose-kmp/.../components/core/buttons/KoltIosButton.kt' },
            { name: 'KoltIconButton', desc: 'Icon-only clickable trigger', path: 'libs/compose-kmp/.../components/core/buttons/KoltIconButton.kt' },
            { name: 'KoltImageButton', desc: 'Button with a leading image/icon aligned with text', path: 'libs/compose-kmp/.../components/core/buttons/KoltImageButton.kt' },
            { name: 'KoltTextButton / KoltLinkButton', desc: 'Plain text actions and hyperlink-styled buttons', path: 'libs/compose-kmp/.../components/core/buttons/KoltTextButton.kt' },
        ],
        usage: `KoltButton(text = uiText, buttonStyle = ButtonStyle.primary(), onClick = { ... })
TextButton(text = uiText, buttonStyle = ButtonStyle.transparent(), onClick = { ... })
KoltIconButton(icon = uiImage, onClick = { ... })
KoltImageButton(image = uiImage, onClick = { ... })

// ButtonStyle factories (all @Composable):
ButtonStyle.primary()           // filled primary
ButtonStyle.secondary()         // outlined
ButtonStyle.transparent()       // text-only
ButtonStyle.primaryNegative()   // muted / de-emphasised
ButtonStyle.primaryPositive()   // accent / confirmed action`,
    },
    {
        id: 'text-helpers-components', category: 'core', demoAnchor: 'demo-text-helpers-components',
        title: 'Text Helpers & Components',
        description: 'Specialized text widgets: click-to-copy, ellipsis with "read more" expansion, and inline image/text alignment.',
        composables: [
            { name: 'KoltText', desc: 'Basic text wrapper supporting selectable text containment, font offset adjust, and HTML strings', path: 'libs/compose-kmp/.../components/core/text/KoltText.kt' },
            { name: 'KoltCopyableText', desc: 'Click a text block to copy it to the clipboard', path: 'libs/compose-kmp/.../components/core/text/KoltCopyableText.kt' },
            { name: 'KoltExpandableText', desc: 'Limits text to N lines with a click-to-expand toggle', path: 'libs/compose-kmp/.../components/core/text/KoltExpandableText.kt' },
            { name: 'KoltImageText', desc: 'Perfectly centered inline icon/image + text alignment', path: 'libs/compose-kmp/.../components/core/text/KoltImageText.kt' },
            { name: 'KeyValuePairText / PrefixedText', desc: 'Formatted text rows for summary views', path: 'libs/compose-kmp/.../components/core/text/KeyValuePairText.kt' },
        ],
        usage: `KoltText(text = uiText, style = Kolt.typography.body.medium)
KeyValuePairText(key = uiText, value = uiText)
PrefixedText(prefix = uiText, value = uiText)
KoltImageText(image = uiImage, text = uiText)`,
    },
    {
        id: 'highlight-search-text', category: 'core', demoAnchor: 'demo-highlight-search-text',
        title: 'Highlight Search Text',
        description: 'Text view that highlights instances matching a search query — type in the embedded demo below to try it.',
        composables: [
            { name: 'KoltHighlightText', desc: 'Highlights matching terms inside a target text block', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/text/KoltHighlightText.kt' },
        ],
        usage: `KoltHighlightText(
    text = "Kotlin Multiplatform simplifies cross-platform sharing.",
    query = searchQuery,
    highlightStyle = SpanStyle(background = Color.Yellow),
)`,
    },
    {
        id: 'images-avatars', category: 'core', demoAnchor: 'demo-images-avatars',
        title: 'Images & Avatars',
        description: 'Images, icons, and initials avatars with automatic high-contrast tinting.',
        composables: [
            { name: 'KoltIcon', desc: 'Standalone static system icon component', path: 'libs/compose-kmp/.../components/core/image/KoltIcon.kt' },
            { name: 'KoltImage', desc: 'Standalone static graphic image wrapper (resource/URL/painter)', path: 'libs/compose-kmp/.../components/core/image/KoltImage.kt' },
            { name: 'KoltAvatar', desc: 'Circular name-initials avatar badge', path: 'libs/compose-kmp/.../components/core/image/KoltAvatar.kt (+ .android/.desktop/.ios actuals)' },
            { name: 'CircleIconBox', desc: 'Centered icon tile with automatic contrast background', path: 'libs/compose-kmp/.../components/core/image/CircleIconBox.kt' },
        ],
        usage: `KoltImage(image = uiImage, modifier = Modifier.size(48.dp))
KoltIcon(image = uiImage, modifier = Modifier.size(24.dp))
KoltAvatar(initials = "AS")`,
    },

    // ---- Inputs & Controls ----
    {
        id: 'themed-form-fields', category: 'inputs', demoAnchor: 'demo-themed-form-fields',
        title: 'Themed Form Fields',
        description: 'Premium text field inputs with full focus borders, clear functionality, password visibility, and validation feedback.',
        composables: [
            { name: 'KoltTextField', desc: 'Standard single-line text field (active/disabled, floating label)', path: 'libs/compose-utils/.../core/text/textfield/KoltTextField.kt' },
            { name: 'KoltPasswordTextField', desc: 'Text field with a visibility eye-toggle action', path: 'libs/compose-utils/.../core/text/textfield/KoltPasswordTextField.kt' },
            { name: 'KoltSearchTextField', desc: 'Search field with leading lens icon and trailing clear action', path: 'libs/compose-utils/.../core/text/textfield/KoltSearchTextField.kt' },
            { name: 'KoltValidatedTextField', desc: 'Input field with validation rules and success/error feedback', path: 'libs/compose-utils/.../core/text/textfield/KoltValidatedTextField.kt' },
        ],
        usage: `KoltTextField(value, onValueChange, label, ...)
KoltValidatedTextField(state = TextFieldState(...), onValueChange, ...)
// TextFieldState wraps value + error + a list of ValidationRule`,
    },
    {
        id: 'appsdropdowns-dropdown-selection', category: 'inputs', demoAnchor: 'demo-appsdropdowns-dropdown-selection',
        title: 'KoltDropDowns (Dropdown Selection)',
        description: 'Dropdown widgets in text, icon, floating-label, and chip styles.',
        composables: [
            { name: 'KoltTextDropDown', desc: 'Outlined button trigger displaying the active value', path: 'libs/compose-utils/.../core/dropdowns/TextDropDown.kt' },
            { name: 'KoltIconDropDown', desc: 'Settings-style menu list triggered from an icon button', path: 'libs/compose-utils/.../core/dropdowns/IconDropDown.kt' },
            { name: 'KoltDropDown (Floating Label)', desc: 'Text-field design with an animated floating label + chevron', path: 'libs/compose-utils/.../core/dropdowns/KoltDropDown.kt' },
            { name: 'ChipDropDown', desc: 'Pill-shaped filter button selector', path: 'libs/compose-utils/.../core/dropdowns/ChipDropDown.kt' },
        ],
        usage: `KoltDropDown(items = listOf(DropDownItem(...)), selected, onSelected)
ChipDropDown(...)
TextDropDown(label, items, selected, onSelected)
IconDropDown(icon, items, selected, onSelected)`,
    },
    {
        id: 'appsslider-value-controls', category: 'inputs', demoAnchor: 'demo-appsslider-value-controls',
        title: 'KoltSlider (Value Controls)',
        description: 'Premium custom slider supporting interactive value drags, custom min/max/step.',
        composables: [
            { name: 'KoltSlider', desc: 'Drag-active thumb handle to adjust numerical settings', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/KoltSlider.kt' },
        ],
        usage: `KoltSlider(value = sliderValue, onValueChange = { sliderValue = it }, valueRange = 0f..100f)`,
    },
    {
        id: 'appsratingbar-star-rating', category: 'inputs', demoAnchor: 'demo-appsratingbar-star-rating',
        title: 'KoltRatingBar (Star Rating)',
        description: 'Interactive star ratings supporting fractional (half-star) values and read-only display.',
        composables: [
            { name: 'KoltRatingBar', desc: 'Click/tap stars to assign a rating value, or render read-only', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/KoltRatingBar.kt' },
        ],
        usage: `KoltRatingBar(rating = 3.5f, onRatingChanged = { ... }, allowHalfRating = true, starCount = 5)
KoltRatingBar(rating = 3.5f, readOnly = true)`,
    },

    // ---- Indicators & Feedback ----
    {
        id: 'appsstatustag-semantic-tags', category: 'indicators', demoAnchor: 'demo-appsstatustag-semantic-tags',
        title: 'KoltStatusTag (Semantic Tags)',
        description: 'Pill-shaped badges driven by primary, secondary, and semantic success/warning/error intents.',
        composables: [
            { name: 'KoltStatusTag', desc: 'Semantic status pill (success/warning/error/info/primary/neutral)', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/KoltStatusTag.kt' },
        ],
        usage: `KoltStatusTag(text = "Active", intent = StatusIntent.Success)`,
    },
    {
        id: 'appsbadge-appstooltip', category: 'indicators', demoAnchor: 'demo-appsbadge-appstooltip',
        title: 'KoltBadge & KoltTooltip',
        description: 'Notification count badges and interactive long-press tooltips.',
        composables: [
            { name: 'KoltBadge', desc: 'Notification count pill badge, alignable to any corner', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/badge/KoltBadge.kt' },
            { name: 'KoltTooltip', desc: 'Interactive inline information popup on long-press/hover', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/KoltTooltip.kt' },
        ],
        usage: `KoltBadge(count = 5, alignment = BadgeAlignment.TopEnd) { /* anchor content */ }
KoltTooltip(message = uiText, position = TooltipPosition.Top) { /* trigger content */ }`,
    },
    {
        id: 'progress-loaders', category: 'indicators', demoAnchor: 'demo-progress-loaders',
        title: 'Progress & Loaders',
        description: 'Themed linear progress, circular progress, and indeterminate loading states.',
        composables: [
            { name: 'KoltProgressIndicator', desc: 'Linear (determinate) and circular/linear indeterminate progress', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/progress/KoltProgressIndicator.kt' },
            { name: 'FullscreenLoader', desc: 'Full-screen blocking loader overlay', path: 'libs/compose-utils/.../core/progress/FullscreenLoader.kt' },
        ],
        usage: `KoltProgressIndicator.Linear(progress = 0.5f)
KoltProgressIndicator.CircularIndeterminate()
FullscreenLoader(isVisible = state.isLoading)`,
    },
    {
        id: 'shimmer-skeleton-empty-states', category: 'indicators', demoAnchor: 'demo-shimmer-skeleton-empty-states',
        title: 'Shimmer Skeleton & Empty States',
        description: 'Smooth loading placeholders with a moving gradient, and clean empty-state templates.',
        composables: [
            { name: 'ShimmerBox / Modifier.shimmerEffect()', desc: 'Loading shimmer placeholder', path: 'libs/compose-kmp/.../components/core/ShimmerBox.kt, .../modifiers/ShimmerEffect.kt' },
            { name: 'KoltEmptyState', desc: 'Centered icon + title + description + action-button template', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/KoltEmptyState.kt' },
        ],
        usage: `Box(Modifier.shimmerEffect())   // apply to any placeholder shape
KoltEmptyState(icon = uiImage, title = uiText, description = uiText, actionLabel = uiText, onAction = { ... })`,
    },
    {
        id: 'banners-system-feedback', category: 'indicators', demoAnchor: 'demo-banners-system-feedback',
        title: 'Banners & System Feedback',
        description: 'Themed alert banners, system snackbars, and toast alerts — driven from a host controller and one-shot Effects.',
        composables: [
            { name: 'KoltBanner', desc: 'Status alert banner with an action trigger', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/messages/KoltBanner.kt' },
            { name: 'KoltSnackbar', desc: 'Dynamic snackbar alert with action label', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/messages/KoltSnackbar.kt' },
            { name: 'Toast', desc: 'Lightweight transient toast message', path: 'libs/compose-utils/.../messages/Toast.kt' },
        ],
        usage: `KoltBanner(title = uiText, message = uiText, intent = BannerIntent.Info, onAction = { ... })

// Collect one-shot effects (lifecycle-aware) in the Route:
vm.collectEffects { effect -> when (effect) {
    is MyEffect.ShowToast -> showToast(effect.msg)
} }`,
    },

    // ---- Layouts & Containers ----
    {
        id: 'appsdivider-spacers', category: 'layouts', demoAnchor: 'demo-appsdivider-spacers',
        title: 'KoltDivider & Spacers',
        description: 'Visual and layout helpers for cleanly separating components — horizontal/vertical dividers plus fixed spacers.',
        composables: [
            { name: 'KoltDivider', desc: 'Horizontal or vertical themed divider line', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/KoltDivider.kt' },
            { name: 'Spacers', desc: 'Fixed-size vertical/horizontal spacing helpers', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/Spacers.kt' },
        ],
        usage: `KoltDivider(orientation = DividerOrientation.Horizontal)
VerticalSpacer(16.dp)
HorizontalSpacer(16.dp)`,
    },
    {
        id: 'texttitledcardview-smooth-corners', category: 'layouts', demoAnchor: 'demo-texttitledcardview-smooth-corners',
        title: 'TextTitledCardView & Smooth Corners',
        description: 'Curvature-continuity (G2) mathematically blended squircle corners — compare against standard CSS/Compose rounded corners in the embedded demo.',
        composables: [
            { name: 'SmoothCornerShape', desc: 'G2-continuous squircle Shape, configurable radius + smoothness', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/theme/SmoothCornerShape.kt' },
            { name: 'TitledCardView', desc: 'Card with a titled header using SmoothCornerShape', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/containers/TitledCardView.kt' },
        ],
        usage: `Box(Modifier.clip(SmoothCornerShape(radius = 24.dp, smoothness = 0.55f)))
TitledCardView(title = uiText) { /* content */ }`,
    },
    {
        id: 'generic-titledcardview', category: 'layouts', demoAnchor: 'demo-generic-titledcardview',
        title: 'Generic TitledCardView',
        description: 'TitledCardView using a custom title slot directly, allowing arbitrary layouts in the header block.',
        composables: [
            { name: 'TitledCardView (titleSlot)', desc: 'Same card container, with a fully custom composable header slot', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/containers/TitledCardView.kt' },
        ],
        usage: `TitledCardView(titleSlot = { Row { Text("🏠"); Text("Custom Title") } }) { /* content */ }`,
    },
    {
        id: 'appsaccordion-collapsible-panels', category: 'layouts', demoAnchor: 'demo-appsaccordion-collapsible-panels',
        title: 'KoltAccordion (Collapsible Panels)',
        description: 'Sleek expandable panels with G2 smooth corners and animated chevron rotation.',
        composables: [
            { name: 'KoltAccordion', desc: 'Expandable/collapsible panel list, single- or multi-expand', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/KoltAccordion.kt' },
        ],
        usage: `KoltAccordion(
    items = listOf(AccordionItem(title = uiText, content = { Text("...") })),
    singleExpand = true,
)`,
    },
    {
        id: 'scaffolds-page-structures', category: 'layouts', demoAnchor: 'demo-scaffolds-page-structures',
        title: 'Scaffolds & Page Structures',
        description: 'PageScaffold (toolbar + content view) and KoltDrawerScaffold (toolbar + sliding navigation drawer).',
        composables: [
            { name: 'KoltPageScaffold', desc: 'Standard screen scaffold: top bar + navigation mode + content', path: 'libs/compose-utils/.../containers/PageScaffold.kt' },
            { name: 'KoltDrawerScaffold', desc: 'Scaffold with a sliding navigation drawer', path: 'libs/compose-utils/.../containers/KoltDrawerScaffold.kt' },
        ],
        usage: `KoltPageScaffold(
    title = AppBarTitle.Text(uiText),
    navigationMode = NavigationMode.Back,
    onNavigateBack = { ... },
) { /* ColumnScope content */ }

KoltDrawerScaffold(drawerContent = { ... }) { /* content */ }`,
    },

    // ---- Complex Systems ----
    {
        id: 'swipeableactionsbox-list-item-gestures', category: 'complex', demoAnchor: 'demo-swipeableactionsbox-list-item-gestures',
        title: 'SwipeableActionsBox (List Item Gestures)',
        description: 'Swipe left/right on list items to reveal context-specific actions (e.g. Archive, Delete) — drag the demo cards below.',
        composables: [
            { name: 'SwipeableActionsBox', desc: 'Swipe-to-reveal action container for list rows', path: 'libs/compose-utils/src/main/java/io/github/koltalabs/kolt/composeutils/components/containers/swipeactionbox/SwipeableActionsBox.kt' },
            { name: 'SwipeAction', desc: 'Single swipe action definition (icon, background, onSwipe)', path: 'libs/compose-utils/.../swipeactionbox/SwipeAction.kt' },
        ],
        usage: `SwipeableActionsBox(
    startActions = listOf(SwipeAction(icon = ..., background = Color(0xFF10B981), onSwipe = { archive() })),
    endActions = listOf(SwipeAction(icon = ..., background = Color(0xFFEF4444), onSwipe = { delete() })),
) { /* row content */ }`,
    },
    {
        id: 'asyncstatebox-state-machine', category: 'complex', demoAnchor: 'demo-asyncstatebox-state-machine',
        title: 'AsyncStateBox State Machine',
        description: 'Renders a dedicated slot depending on the Idle / Loading / Success / Error state of an async operation.',
        composables: [
            { name: 'AsyncStateBox', desc: 'Slot-based renderer driven by an AsyncState value', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/AsyncStateBox.kt' },
            { name: 'AsyncState', desc: 'Sealed class: Idle / Loading / Success / Error', path: 'libs/utils/.../state/AsyncState.kt' },
        ],
        usage: `AsyncStateBox(
    state = viewModel.dashboardState,
    loading = { KoltCircularProgress() },
    error = { e -> KoltEmptyState(title = e.message?.let(UiText::DynamicString)) },
) { data -> DashboardContent(data) }`,
    },
    {
        id: 'smartpulltorefreshbox', category: 'complex', demoAnchor: 'demo-smartpulltorefreshbox',
        title: 'SmartPullToRefreshBox',
        description: 'Draggable viewport that triggers a suspending reload action — pull down on the demo list below.',
        composables: [
            { name: 'PullToRefreshBox', desc: 'Pull-to-refresh container wrapping a scrollable list', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/containers/PullToRefreshBox.kt' },
        ],
        usage: `PullToRefreshBox(isRefreshing = state.isRefreshing, onRefresh = { vm.dispatch(Intent.Refresh) }) {
    LazyColumn { items(feed) { FeedItem(it) } }
}`,
    },
    {
        id: 'appsstepper-wizard-flows', category: 'complex', demoAnchor: 'demo-appsstepper-wizard-flows',
        title: 'KoltStepper (Wizard Flows)',
        description: 'Flow-based progress layout with node pulsing and animated transition lines between steps.',
        composables: [
            { name: 'KoltStepper', desc: 'Horizontal or vertical multi-step progress indicator', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/KoltStepper.kt' },
        ],
        usage: `KoltStepper(
    steps = listOf(StepItem("Verification"), StepItem("Payment"), StepItem("Confirm")),
    activeStep = 1,
    orientation = StepperOrientation.Horizontal,
)`,
    },
    {
        id: 'native-overlay-controllers', category: 'complex', demoAnchor: 'demo-native-overlay-controllers',
        title: 'Native Overlay Controllers',
        description: 'Dialog and bottom-sheet overlay controllers, available on all KMP targets, using SmoothCornerShape for G2-continuous corners.',
        composables: [
            { name: 'MessageDialog', desc: 'Themed alert dialog with positive/negative buttons', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/messages/MessageDialog.kt' },
            { name: 'KoltBottomSheet', desc: 'Modal bottom sheet with a drag handle', path: 'libs/compose-kmp/src/commonMain/kotlin/io/github/koltalabs/kolt/composekmp/components/core/messages/KoltBottomSheet.kt' },
        ],
        usage: `MessageDialog(
    title = uiText, message = uiText,
    positiveButton = DialogButtonStyle(label = uiText, onClick = { ... }),
    negativeButton = DialogButtonStyle(label = uiText, onClick = { ... }),
    onDismiss = { ... },
)

KoltBottomSheet(visible = sheetVisible, onDismiss = { sheetVisible = false }) { /* sheet content */ }`,
    },
    {
        id: 'in-app-updates-update-utils', category: 'complex', demoAnchor: 'demo-in-app-updates-update-utils',
        title: 'In-App Updates (update-utils)',
        description: 'Android-only Flexible/Immediate update prompts driven by Firebase Remote Config + the Play Store In-App Update API.',
        composables: [
            { name: 'AppUpdateHelperUtil', desc: 'ViewModel delegate gating forced/optional update dialogs', path: 'libs/update-utils/src/main/java/io/github/koltalabs/kolt/updateutils/AppUpdateHelperUtil.kt' },
        ],
        usage: `class MainViewModel @Inject constructor(...) :
    MviViewModel<MainState, MainIntent, MainEffect>(MainState()),
    AppUpdateHelperUtil by AppUpdateHelperUtilImpl() { ... }

@Composable
fun AppRoot(vm: MainViewModel = hiltViewModel()) {
    vm.CheckForUpdateAndSetContent(
        content = { /* NavHost */ },
        onForceUpdate = { /* open Play Store */ },
    )
}`,
    },
];

export const CATEGORIES = [
    { 
        id: 'core', 
        label: 'Core', 
        icon: '📦', 
        tabId: 'core-tab',
        categoryIntro: 'Fundamental Compose layout and widget primitives. These components handle low-level operations such as custom button styling, click-to-copy, expandable text limits, and high-contrast avatar placeholders.'
    },
    { 
        id: 'inputs', 
        label: 'Inputs & Controls', 
        icon: '🎛️', 
        tabId: 'inputs-tab',
        categoryIntro: 'Interactive form elements and control widgets. These components handle standard text entry, custom password visibilities, numerical slider adjustments, star-ratings, and dropdown lists.'
    },
    { 
        id: 'indicators', 
        label: 'Indicators & Feedback', 
        icon: '🏷️', 
        tabId: 'indicators-tab',
        categoryIntro: 'Visual cues and semantic system response widgets. These components provide lightweight progress animations, tooltip labels, warning and error banners, shimmer skeleton loading states, and custom badges.'
    },
    { 
        id: 'layouts', 
        label: 'Layouts & Containers', 
        icon: '📐', 
        tabId: 'layouts-tab',
        categoryIntro: 'Containers and layout grids. These components handle page structures, side-sliding drawers, dividers, accordions, and custom cards featuring curvature-continuous smooth corner shapes.'
    },
    { 
        id: 'complex', 
        label: 'Complex Systems', 
        icon: '⚡', 
        tabId: 'complex-tab',
        categoryIntro: 'Advanced state machines and gesture-driven UI components. These components coordinate state transitions, drag-to-refresh feeds, list row actions, and Play Store in-app update checks.'
    },
];

export function getComponentById(id) {
    return COMPONENTS.find(c => c.id === id);
}
