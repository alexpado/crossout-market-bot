package xo.marketbot.configurations.interfaces;

public interface IEmojiConfiguration {

    String getStableEmoji();

    String getUpGreenEmoji();

    String getDownGreenEmoji();

    String getUpRedEmoji();

    String getDownRedEmoji();

    default String with(double newValue, double oldValue, boolean downIsBad) {

        if (newValue == oldValue) {
            return this.getStableEmoji();
        } else if (newValue < oldValue) {
            return downIsBad ? this.getDownRedEmoji() : this.getDownGreenEmoji();
        } else {
            return downIsBad ? this.getUpGreenEmoji() : this.getUpRedEmoji();
        }
    }

}
