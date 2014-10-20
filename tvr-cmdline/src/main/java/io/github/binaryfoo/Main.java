package io.github.binaryfoo;

import io.github.binaryfoo.decoders.DecodeSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            printHelp();
            System.exit(1);
        }
        String tag = args[0];
        String value = args[1];
        String meta = args.length > 2 ? args[2] : "EMV";

        RootDecoder rootDecoder = new RootDecoder();
        DecodeSession decodeSession = new DecodeSession();
        decodeSession.setTagMetaData(rootDecoder.getTagMetaData(meta));
        TagInfo tagInfo = RootDecoder.getTagInfo(tag);
        if (value.equals("-")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) {
                decodeValue(line, decodeSession, tagInfo);
            }
        } else {
            decodeValue(value, decodeSession, tagInfo);
        }
    }

    private static void decodeValue(String value, DecodeSession decodeSession, TagInfo tagInfo) {
        List<DecodedData> decoded = tagInfo.getDecoder().decode(value, 0, decodeSession);
        new DecodedWriter(System.out).write(decoded, "");
    }

    private static void printHelp() {
        System.out.println("Usage Main <decode-type> <value> [<tag-set>]");
        System.out.println("  <decode-type> is one of");
        for (TagInfo tag: RootDecoder.getSupportedTags()){
            System.out.println("    " + tag.getShortName() + ": " + tag.getLongName());
        }
        System.out.println("  <value> is the hex string or '-' for standard input");
        System.out.println("  <tag-set> is one of " + RootDecoder.getAllTagMeta() + " defaults to EMV");
    }
}
