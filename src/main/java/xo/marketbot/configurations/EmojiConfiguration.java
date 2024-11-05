package xo.marketbot.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import xo.marketbot.configurations.interfaces.IEmojiConfiguration;

@Configuration
public class EmojiConfiguration implements IEmojiConfiguration {

    @Value("${discord.emoji.stable}")
    private String emojiStable;

    @Value("${discord.emoji.up.green}")
    private String emojiUpGreen;

    @Value("${discord.emoji.up.red}")
    private String emojiUpRed;

    @Value("${discord.emoji.down.green}")
    private String emojiDownGreen;

    @Value("${discord.emoji.down.red}")
    private String emojiDownRed;

    @Override
    public String getStableEmoji() {

        return this.emojiStable;
    }

    @Override
    public String getUpGreenEmoji() {

        return this.emojiUpGreen;
    }

    @Override
    public String getDownGreenEmoji() {

        return this.emojiDownGreen;
    }

    @Override
    public String getUpRedEmoji() {

        return this.emojiUpRed;
    }

    @Override
    public String getDownRedEmoji() {

        return this.emojiDownRed;
    }

}
