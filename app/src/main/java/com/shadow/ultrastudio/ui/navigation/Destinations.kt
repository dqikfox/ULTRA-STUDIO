package com.shadow.ultrastudio.ui.navigation

object Destinations {
    const val SERVER_SETUP = "server_setup"
    const val LOGIN = "login"
    const val REGISTER = "register"

    /** The bottom-nav shell; a single destination on the outer nav graph. */
    const val MAIN = "main"

    // Tabs within MAIN's own nested NavHost.
    const val MAIN_CREATE = "main_create"
    const val MAIN_GALLERY = "main_gallery"
    const val MAIN_QUEUE = "main_queue"
    const val MAIN_SETTINGS = "main_settings"

    /** Lightbox - a full-screen route on the outer graph, so the bottom nav hides. The
     * tapped item travels via SelectedItemHolder, not route arguments. */
    const val ITEM_DETAIL = "item_detail"

    /** Pushed from Settings, outer graph so the bottom nav hides. */
    const val ADMIN = "admin"

    /** The Nyx AI companion, reachable from any main tab, outer graph so the bottom nav hides. */
    const val NYX = "nyx"
}
