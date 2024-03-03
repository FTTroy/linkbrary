package com.github.FTTroy.linkbrary.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {

    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    public static String setHttpPrefix(String content) {
        return !content.startsWith(Constants.PREFIX) ? Constants.PREFIX.concat(content) : content;
    }

    public static void fileLogger(String fileContent) throws IOException {
        File file = new File(FileUtils.getUserDirectoryPath().concat(Constants.LOG_FILE_NAME));
        FileWriter writer = new FileWriter(file.getAbsolutePath(), true);
        writer.write("\n" + LocalDateTime.now() + " " + fileContent);
        writer.flush();
        writer.close();
        logger.info("Check file ".concat(file.getAbsolutePath()).concat(" for more details"));
    }
}

