# Crossout Market Bot

Crossout Market Bot is a Discord bot designed to bring the power of CrossoutDB directly into your Discord server. It
provides real-time price information, allows item searches, and even lets you set up customizable "watchers" that alert
you when item prices meet your criteria.

## Features

- **Item Search:** Retrieve item price information from CrossoutDB directly on Discord.
- **Price Charts:** View historical price trends for items in a simple, easy-to-read chart format.
- **Price Watchers:** Set up alerts to monitor item prices and receive DMs when they hit specified thresholds.

No planned features for now, but contributions and suggestions are always welcome!

## Setup and Installation

> Make sure Docker and Docker Compose are installed on your machine.

- Clone the repository to your local machine:
- Navigate into the project directory:
- Copy the `.env.example` file to `.env`:
- Fill in the .env file with your Discord bot token and emoji settings:
    - **Discord Bot Token:** Obtain this by creating a bot on the Discord Developer Portal.
    - **Emoji Setup:** Upload the necessary emojis from `src/main/resources/emoji` to your Discord application and
      reference them in the `.env` file.
- Run the bot using `docker-compose up -d`

### Alternative Setup (Without Docker)

You'll need to set up a MariaDB server and configure `application.properties` accordingly. You can also use IDE
plugins (like EnvFile for IntelliJ IDEA) to load the `.env` configuration for running the project.

## Usage

All commands are accessible through Discord's slash command interface. Users can view a list of available commands and
get usage details directly within Discord.

> Important: The bot doesn't require special permissions to run, but for the watcher feature to work, users must allow
> DMs
> from the bot.

## Contribution Guidelines

Contributions are welcome! If you'd like to add new features or improve existing functionality:

- **Open an Issue:** Please discuss your idea first to make sure it aligns with the project goals.
- **Fork the Repository:** Make your changes in a new branch.
- **Submit a Pull Request:** Describe your changes clearly and reference any related issues.

## Setting Up a Development Environment

Clone the repo and open it in a Java-capable IDE (with Gradle support).

## Notes and Recommendations

- Moderation in API Use: While there are no strict rate limits on CrossoutDB, please use the API responsibly to avoid
  unnecessary load on the community-maintained infrastructure.
- Watcher Limits: While there is no set limit for watchers, keep in mind that excessive watchers might trigger Discord
  DM rate limits (and every watcher will query the CrossoutDB API)
