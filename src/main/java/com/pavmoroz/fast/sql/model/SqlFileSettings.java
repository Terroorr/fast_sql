package com.pavmoroz.fast.sql.model;

public class SqlFileSettings {

    private String author;
    private String release;
    private String ticketType;
    private String ticketNumber;
    private String description;
    private String project;
    private String db;
    private String descriptionInFileName;

    private SqlFileSettings() {

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
        return new SqlFileSettings().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setAuthor(String author) {
            SqlFileSettings.this.author = author;

            return this;
        }

        public Builder setRelease(String release) {
            SqlFileSettings.this.release = release;

            return this;
        }

        public Builder setTicketType(String ticketType) {
            SqlFileSettings.this.ticketType = ticketType;

            return this;
        }

        public Builder setTicketNumber(String ticketNumber) {
            SqlFileSettings.this.ticketNumber = ticketNumber;

            return this;
        }

        public Builder setDescription(String description) {
            SqlFileSettings.this.description = description;

            return this;
        }

        public Builder setProject(String project) {
            SqlFileSettings.this.project = project;

            return this;
        }

        public Builder setDb(String db) {
            SqlFileSettings.this.db = db;

            return this;
        }

        public Builder setDescriptionInFileName(String descriptionInFileName) {
            SqlFileSettings.this.descriptionInFileName = descriptionInFileName;

            return this;
        }

        public SqlFileSettings build() {
            return SqlFileSettings.this;
        }
    }
}
