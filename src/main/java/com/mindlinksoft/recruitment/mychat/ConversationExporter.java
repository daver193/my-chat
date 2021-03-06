package com.mindlinksoft.recruitment.mychat;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a conversation exporter that can read a conversation and write it out in JSON.
 */
public class ConversationExporter {

    private static String keyword;
    private static String invisibleUser;
    private static String blacklistedWord;

    /**
     * The application entry point.
     * @param args The command line arguments.
     * @throws Exception Thrown when something bad happens.
     */
    public static void main(String[] args) throws Exception {
        ConversationExporter exporter = new ConversationExporter();
        ConversationExporterConfiguration configuration = new CommandLineArgumentParser().parseCommandLineArguments(args);



        if (configuration.getInputFilters() != null){

            List<String> filterList = Arrays.asList(configuration.getInputFilters().split(","));

            System.out.println(filterList);

            System.out.println(filterList.indexOf("dataflt=Hello"));



            for (String filter : filterList){
                if (filter.contains("dataflt")){
                    keyword = filter.split("=")[1];
                }
                else if (filter.contains("usr")){
                    invisibleUser = filter.split("=")[1];
                }
                else if (filter.contains("blacklst")){
                    blacklistedWord = filter.split("=")[1];
                }
            }

        }

        exporter.exportConversation(configuration.getInputFilePath(), configuration.getOutputFilePath());
    }

    /**
     * Exports the conversation at {@code inputFilePath} as JSON to {@code outputFilePath}.
     * @param inputFilePath The input file path.
     * @param outputFilePath The output file path.
     * @throws Exception Thrown when something bad happens.
     */
    public void exportConversation(String inputFilePath, String outputFilePath) throws Exception {
        Conversation conversation = this.readConversation(inputFilePath);

        this.writeConversation(conversation, outputFilePath);

        // TODO: Add more logging...
        System.out.println("Conversation exported from '" + inputFilePath + "' to '" + outputFilePath);
    }

    /**
     * Helper method to write the given {@code conversation} as JSON to the given {@code outputFilePath}.
     * @param conversation The conversation to write.
     * @param outputFilePath The file path where the conversation should be written.
     * @throws Exception Thrown when something bad happens.
     */
    public void writeConversation(Conversation conversation, String outputFilePath) throws Exception {

        try(FileWriter writer = new FileWriter(outputFilePath))
        {
            Gson json = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Instant.class, new InstantSerializer())
                    .create();

            writer.write(json.toJson(conversation.getName()) + "\n");
            for (Message ms : conversation.getMessages()){
                writer.write(json.toJson(ms.getTimestamp() + " " + ms.getTimestamp() + " " + ms.getContent()) + "\n");
            }

            writer.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("The file " + outputFilePath + " was not found.");
        } catch (EOFException e){
            e.printStackTrace();
            throw new Exception("End of the file " + outputFilePath + " reached.");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
            throw new Exception("The file " + outputFilePath + " has unsupported encoding.");
        } catch (SocketException e){
            e.printStackTrace();
            throw new Exception("The socket connection with the file " + outputFilePath + " terminated unexpectedly.");
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new Exception("An unknown error occurred for the file "+ outputFilePath);
        }
    }

    /**
     * Represents a helper to read a conversation from the given {@code inputFilePath}.
     * @param inputFilePath The path to the input file.
     * @return The {@link Conversation} representing by the input file.
     * @throws Exception Thrown when something bad happens.
     */
    public Conversation readConversation(String inputFilePath) throws Exception {
        try(InputStream is = new FileInputStream(inputFilePath);
            BufferedReader r = new BufferedReader(new InputStreamReader(is))) {

            List<Message> messages = new ArrayList<Message>();

            String conversationName = r.readLine();
            String line;

            FilterHandler filterHandler = new FilterHandler();

            while ((line = r.readLine()) != null) {
                String[] split = line.split(" ");

                // Check if messages are filtered by user
                if (filterHandler.filterByUser(invisibleUser, split[1])){
                    continue;
                }

                String msg = "";
                for (int i = 2; i < split.length; i++){

                    if (filterHandler.isBlacklisted(blacklistedWord, split[i])){
                        msg = msg.concat(" *redacted*");
                    }
                    else {
                        msg = msg.concat(" " + split[i]);
                    }
                }

                if (!filterHandler.filterByKeyword(keyword, msg)){
                    continue;
                }

                messages.add(new Message(Instant.ofEpochSecond(Long.parseUnsignedLong(split[0])), split[1], msg));
            }

            return new Conversation(conversationName, messages);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("The file: " + inputFilePath + "was not found.");
        } catch (EOFException e){
            e.printStackTrace();
            throw new Exception("End of the file " + inputFilePath + " reached.");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
            throw new UnsupportedEncodingException("The file " + inputFilePath + " has unsupported encoding.");
        } catch (SocketException e){
            e.printStackTrace();
            throw new SocketException("The socket connection with the file " + inputFilePath + " terminated unexpectedly.");
        }catch (IOException e) {
            throw new Exception("An unknown error occurred for the file "+ inputFilePath);
        }
    }

    class InstantSerializer implements JsonSerializer<Instant> {
        @Override
        public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(instant.getEpochSecond());
        }
    }
}
