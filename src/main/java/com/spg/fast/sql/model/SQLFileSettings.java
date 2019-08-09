package com.spg.fast.sql.model;

public class SQLFileSettings {

    private String author;
    private String release;
    private String ticketType;
    private String ticketNumber;
    private String description;
    private String project;
    private String db;
    private String descriptionInFileName;

    private SQLFileSettings() {
    }

    public String getAuthor() {
        return author;
    }

    public String getRelease() {
        return release;
    }

    public String getTicketType() {
        return ticketType;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getProject() {
        return project;
    }

    public String getDb() {
        return db;
    }

    public String getDescriptionInFileName() {
        return descriptionInFileName;
    }

    public static Builder newBuilder() {
        return new SQLFileSettings().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setAuthor(String author) {
            SQLFileSettings.this.author = author;

            return this;
        }

        public Builder setRelease(String release) {
            SQLFileSettings.this.release = release;

            return this;
        }

        public Builder setTicketType(String ticketType) {
            SQLFileSettings.this.ticketType = ticketType;

            return this;
        }

        public Builder setTicketNumber(String ticketNumber) {
            SQLFileSettings.this.ticketNumber = ticketNumber;

            return this;
        }

        public Builder setDescription(String description) {
            SQLFileSettings.this.description = description;

            return this;
        }

        public Builder setProject(String project) {
            SQLFileSettings.this.project = project;

            return this;
        }

        public Builder setDb(String db) {
            SQLFileSettings.this.db = db;

            return this;
        }

        public Builder setDescriptionInFileName(String descriptionInFileName) {
            SQLFileSettings.this.descriptionInFileName = descriptionInFileName;

            return this;
        }

        public SQLFileSettings build() {
            return SQLFileSettings.this;
        }
    }
}
