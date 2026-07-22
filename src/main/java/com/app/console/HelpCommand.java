package com.app.console;

import com.app.service.CommunityUseCases;

public class HelpCommand extends Command {

    @Override
    public void execute(String[] args) {
        System.out.println("--This is the Help Page for Reddit-CLI--");
        System.out.println("1 - Create user");
        System.out.println("2 - Log into account");
        System.out.println("3 - Log out of account");
        System.out.println("4 - Post on a community");
        System.out.println("5 - Comment on a post");
        System.out.println("6 - Create community");
        System.out.println("7 - List all users");
        System.out.println("8 - List all posts of a community");
        System.out.println("9 - List all comments of a post");
        System.out.println("10 - List all communities");
        System.out.println("11 - Delete a user");
        System.out.println("12 - Delete a post");
        System.out.println("13 - Delete a comment");
        System.out.println("14 - Delete a community");
        System.out.println("15 - Remove a post from community");
        System.out.println("16 - Exit a community");
        System.out.println("17 - Join an existing community");
        System.out.println("18 - Edit a community Description");
        System.out.println("19 - Find a community");
        System.out.println("20 - Edit a comment");
        System.out.println("Help / h - Display this Help Menu with all existing commands");
        System.out.println("0. Exit the application\n");
    }
}
