package com.coinliquidity.web.persist;

import com.coinliquidity.web.model.LiquidityData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

import static org.apache.commons.io.FilenameUtils.removeExtension;

public class FilePersister implements LiquidityDataPersister {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilePersister.class);

    private final Path dataDir;
    private final ObjectMapper mapper;

    public FilePersister(String dataDir) {
        this.dataDir = Paths.get(dataDir);
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void persist(final LiquidityData liquidityData) {
        final String filename = getFilename(liquidityData.getUpdateTime());
        final File outFile = dataDir.resolve(filename).toFile();
        try {
            LOGGER.info("Writing data to " + outFile);
            mapper.writeValue(outFile, liquidityData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFilename(final Instant instant) {
        return instant.getEpochSecond() + ".json";
    }

    @Override
    public Optional<LiquidityData> loadLatest() {
        Path latest = dataDir;
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dataDir)) {
            for (final Path path : directoryStream) {
                if (path.compareTo(latest) > 0) {
                    latest = path;
                }
            }

            return dataDir.equals(latest) ? Optional.empty() : Optional.of(fromPath(latest));
        } catch (IOException e) {
            LOGGER.error("Could not load latest data", e);
            return Optional.empty();
        }
    }

    public List<LiquidityData> loadHistory(final Instant threshold) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dataDir)) {
            final List<LiquidityData> data = new ArrayList<>();
            for (final Path path : directoryStream) {
                final long timestamp = Long.parseLong(removeExtension(path.toFile().getName()));
                if (Instant.ofEpochSecond(timestamp).isAfter(threshold)) {
                    data.add(fromPath(path));
                }
            }
            data.sort(Comparator.comparing(LiquidityData::getUpdateTime));
            return data;
        } catch (IOException e) {
            LOGGER.error("Could not load history data", e);
            return Collections.emptyList();
        }
    }

    private LiquidityData fromPath(final Path path) throws IOException {
        return mapper.readValue(path.toFile(), LiquidityData.class);
    }
}
