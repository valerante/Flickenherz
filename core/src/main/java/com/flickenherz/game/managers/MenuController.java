package com.flickenherz.game.managers;

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

    public void selectCurrentMenu() {
        audio.playMenuSelect();
    }

    public String getSelectedChoice() {
        return getMenuItems()[selectedIndex];
    }

    public void enterAttackSubmenu() {
        menuState = MenuState.ATTACK_SUBMENU;
        selectedIndex = 0;
    }

    public void exitSubmenu() {
        menuState = MenuState.MAIN_MENU;
        selectedIndex = 0;
    }

    public boolean isInAttackSubmenu() {
        return menuState == MenuState.ATTACK_SUBMENU;
    }

    public MenuState getMenuState() {
        return menuState;
    }

    public void setMenuState(MenuState state) {
        this.menuState = state;
    }

    public boolean isInMainMenu() {
        return menuState == MenuState.MAIN_MENU;
    }

    public boolean isInEnemyTurn() {
        return menuState == MenuState.ENEMY_TURN;
    }

    public boolean isGameOver() {
        return menuState == MenuState.WIN || menuState == MenuState.LOSE;
    }

    public boolean isWon() {
        return menuState == MenuState.WIN;
    }

    public void reset() {
        selectedIndex = 0;
        menuState = MenuState.MAIN_MENU;
    }
}
