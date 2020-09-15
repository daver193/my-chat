package com.mindlinksoft.recruitment.mychat;

public class FilterHandler {

    public boolean filterByUser(String user, String author){
        if (user != null){
            return author.contains(user);
        }
        return false;
    }

    /**
     *     Messages can be filtered by a specific keyword
     *     The keyword can be specified as a command-line argument
     *     All messages sent containing the keyword are written to the JSON output
     *     Messages sent that do not contain the keyword are not written to the JSON output
    */
    public boolean filterByKeyword(String keyword, String message){
        if (keyword != null){
            return (message.contains(keyword));
        }

        return true;
    }

    public boolean isBlacklisted(String blacklistedWord, String content){
        return (content.contains(blacklistedWord));
    }

}
