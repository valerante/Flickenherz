package com.flickenherz.game.managers;

// Steuert Menünavigation und -zustand
public class MenuController {

    public enum MenuState {
        MAIN_MENU, ATTACK_SUBMENU, ENEMY_TURN, WIN, LOSE
    }

    private final String[] menuItems = {"FIGHT", "ITEM"};
    private final String[] attackSubmenuItems = {"SLASH", "STRIKE", "HEAVY", "QUICK"};
    private int selectedIndex = 0;
    private MenuState menuState;
    private final AudioManager audio;

    public MenuController(AudioManager audio) {
        this.audio = audio;
        this.menuState = MenuState.MAIN_MENU;
    }

    public String[] getMenuItems() {
        if (menuState == MenuState.ATTACK_SUBMENU) {
            return attackSubmenuItems;
        }
        return menuItems;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void navigateLeft() {
        String[] currentMenu = getMenuItems();
        selectedIndex = (selectedIndex - 1 + currentMenu.length) % currentMenu.length;
        audio.playMenuMove();
    }

    public void navigateRight() {
        String[] currentMenu = getMenuItems();
        selectedIndex = (selectedIndex + 1) % currentMenu.length;
        audio.playMenuMove();
    }

    /**
     * Plays the selection sound effect when a menu item is chosen.
     */
    public void selectCurrentMenu() {
        audio.playMenuSelect();
    }

    /**
     * Gets the text of the currently selected menu item.
     * 
     * @return String representing the selected menu choice
     */
    public String getSelectedChoice() {
        return getMenuItems()[selectedIndex];
    }

    /**
     * Enters the attack submenu from the main menu.
     * Resets selection index to 0.
     */
    public void enterAttackSubmenu() {
        menuState = MenuState.ATTACK_SUBMENU;
        selectedIndex = 0;
    }

    /**
     * Exits the current submenu and returns to the main menu.
     * Resets selection index to 0.
     */
    public void exitSubmenu() {
        menuState = MenuState.MAIN_MENU;
        selectedIndex = 0;
    }

    /**
     * Checks if the player is currently in the attack submenu.
     * 
     * @return true if in attack submenu, false otherwise
     */
    public boolean isInAttackSubmenu() {
        return menuState == MenuState.ATTACK_SUBMENU;
    }

    /**
     * Gets the current menu state.
     * 
     * @return Current MenuState enum value
     */
    public MenuState getMenuState() {
        return menuState;
    }

    /**
     * Sets the menu state to a specific value.
     * 
     * @param state New MenuState to set
     */
    public void setMenuState(MenuState state) {
        this.menuState = state;
    }

    /**
     * Checks if the menu is in the main menu state.
     * 
     * @return true if in main menu, false otherwise
     */
    public boolean isInMainMenu() {
        return menuState == MenuState.MAIN_MENU;
    }

    /**
     * Checks if the menu is in the enemy turn state.
     * 
     * @return true if enemy turn is active, false otherwise
     */
    public boolean isInEnemyTurn() {
        return menuState == MenuState.ENEMY_TURN;
    }

    /**
     * Checks if the game is over (either win or lose).
     * 
     * @return true if game is over, false otherwise
     */
    public boolean isGameOver() {
        return menuState == MenuState.WIN || menuState == MenuState.LOSE;
    }

    /**
     * Checks if the player has won the game.
     * 
     * @return true if player won, false otherwise
     */
    public boolean isWon() {
        return menuState == MenuState.WIN;
    }

    /**
     * Resets the menu controller to its initial state.
     * Sets selection to 0 and menu state to MAIN_MENU.
     */
    public void reset() {
        selectedIndex = 0;
        menuState = MenuState.MAIN_MENU;
    }
}
