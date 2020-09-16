package com.mindlinksoft.recruitment.mychat;

public class FilterHandler {

    /**
     * Messages can be filtered by a specific user
     * The user can be provided as a command-line argument, eg: usr="bob"
     * All messages sent by the specified user are written to the JSON output
     * Messages sent by any other user are not written to the JSON output
     * @param user The user that must be excluded
     * @param author The author of the given message
     * @return true if the author is the user that asked to be excluded, false otherwise
     */
    public boolean filterByUser(String user, String author){
        if (user != null){
            return author.contains(user);
        }
        return false;
    }

    /**
     *     Messages can be filtered by a specific keyword
     *     The keyword can be specified as a command-line argument, eg: dataflt="pie"
     *     All messages sent containing the keyword are written to the JSON output
     *     Messages sent that do not contain the keyword are not written to the JSON output
     *     @param keyword The keyword that must be included in order to show a message
     *     @param message The message to search in for the keyword
     *     @return true if the given keyword is in the given message, false otherwise
    */
    public boolean filterByKeyword(String keyword, String message){
        if (keyword != null){
            return (message.contains(keyword));
        }
        return true;
    }

    /**
     * Hide specific words
     * A blacklist can be specified as a command-line argument, eg: blacklst="head"
     * Any blacklisted word is replaced with "*redacted*" in the output.
     * @param blacklistedWord The word that is in the blacklist
     * @param content The content to check if it belongs to the black list
     * @return true if the content is a blacklisted word, false otherwise
     */
    public boolean isBlacklisted(String blacklistedWord, String content){
        return (content.contains(blacklistedWord));
    }
}