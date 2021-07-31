package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class WordsData {

    private static final String CONTACTS_FILE = "contacts.xml";
    private static final String DICT_FILE = "testDict.csv";

    private static final String WORDSDATA = "contact";
    private static final String WORD = "first_name";
    private static final String TRANSLATE = "last_name";
    private static final String TRANSCRIPT = "phone_number";
    //private static final String NOTES = "notes";

    private ObservableList<Word> words;

    public WordsData() {
        words = FXCollections.observableArrayList();
    }

    public ObservableList<Word> getContacts() {
        return words;
    }

    public void addWord(Word item) {
        words.add(item);
    }

    public void deleteContact(Word item) {
        words.remove(item);
    }

    public void loadDict() {
        List<String> dictLines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(DICT_FILE));
            while (scanner.hasNextLine()) {
                dictLines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!dictLines.isEmpty()) {
            for (int i = 0; i < dictLines.size(); ++i) {
                String line = dictLines.get(i);
                String[] parts = line.split("\t");
                String word = parts[0];
                int firstSq = parts[1].indexOf("[");
                String transl = parts[1].substring(0, firstSq);
                String transcript = parts[1].substring(firstSq).trim();
                words.add(new Word(word, transl, transcript));
            }
        }
    }

    /*
    public void loadContacts() {
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(CONTACTS_FILE);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Word word = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have a contact item, we create a new contact
                    if (startElement.getName().getLocalPart().equals(WORDSDATA)) {
                        word = new Word();
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(WORD)) {
                            event = eventReader.nextEvent();
                            word.setFirstName(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(TRANSLATE)) {
                        event = eventReader.nextEvent();
                        word.setLastName(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(TRANSCRIPT)) {
                        event = eventReader.nextEvent();
                        word.setPhoneNumber(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(NOTES)) {
                        event = eventReader.nextEvent();
                        word.setNotes(event.asCharacters().getData());
                        continue;
                    }
                }

                // If we reach the end of a contact element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(WORDSDATA)) {
                        words.add(word);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

     */

    public void saveDict() throws IOException {
        PrintWriter fd = new PrintWriter(new FileWriter("dictionary.csv"));
        fd.println("word\ttranslation transcription");
        for (Word word : words) {
            fd.println(word.toString());
        }
        fd.close();
    }

    /*
    public void saveContacts() {

        try {
            // create an XMLOutputFactory
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // create XMLEventWriter
            XMLEventWriter eventWriter = outputFactory
                    .createXMLEventWriter(new FileOutputStream(CONTACTS_FILE));
            // create an EventFactory
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement contactsStartElement = eventFactory.createStartElement("",
                    "", "contacts");
            eventWriter.add(contactsStartElement);
            eventWriter.add(end);

            for (Word contact : words) {
                saveContact(eventWriter, eventFactory, contact);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "contacts"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Problem with Contacts file: " + e.getMessage());
            e.printStackTrace();
        } catch (XMLStreamException e) {
            System.out.println("Problem writing contact: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void saveContact(XMLEventWriter eventWriter, XMLEventFactory eventFactory, Word contact)
            throws FileNotFoundException, XMLStreamException {

        XMLEvent end = eventFactory.createDTD("\n");

        // create contact open tag
        StartElement configStartElement = eventFactory.createStartElement("",
                "", WORDSDATA);
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        // Write the different nodes
        createNode(eventWriter, WORD, contact.getFirstName());
        createNode(eventWriter, TRANSLATE, contact.getLastName());
        createNode(eventWriter, TRANSCRIPT, contact.getPhoneNumber());
        createNode(eventWriter, NOTES, contact.getNotes());

        eventWriter.add(eventFactory.createEndElement("", "", WORDSDATA));
        eventWriter.add(end);
    }

    private void createNode(XMLEventWriter eventWriter, String name,
                            String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

     */

}
