package xo.marketbot.responses;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xo.marketbot.XoMarketApplication;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;

public class DisplayTemplate extends EmbedBuilder {

    private final TranslationContext context;
    private final JDA                jda;
    private final String             title;

    public DisplayTemplate(TranslationContext context, JDA jda) {

        this.context = context;
        this.jda     = jda;
        this.title   = null;
        this.makeDefaults();
    }

    public DisplayTemplate(TranslationContext context, JDA jda, String title) {

        this.context = context;
        this.jda     = jda;
        this.title   = title;
        this.makeDefaults();
    }

    private void makeDefaults() {

        if (this.title != null) {
            this.setDescription(this.title + "\n──────────────────────────────\n");
        }
        this.setThumbnail("https://crossoutdb.com/img/crossoutdb_logo_compact.png");
        this.setAuthor(
                context.getTranslation(TranslationService.TR_EMBED__INVITE),
                XoMarketApplication.INVITE,
                jda.getSelfUser().getAvatarUrl()
        );
        this.setFooter(null);
    }

    @NotNull
    @Override
    public EmbedBuilder setFooter(@Nullable String text) {

        if (text == null) {
            return super.setFooter(
                    String.format(
                            "%s • %s",
                            context.getTranslation(TranslationService.TR_EMBED__FOOTER_XODB),
                            context.getTranslation(TranslationService.TR_EMBED__FOOTER_DEVELOPER
                            )
                    )
            );
        }

        return super.setFooter(
                String.format(
                        "%s • %s • %s",
                        text,
                        context.getTranslation(TranslationService.TR_EMBED__FOOTER_XODB),
                        context.getTranslation(TranslationService.TR_EMBED__FOOTER_DEVELOPER
                        )
                )
        );
    }

}
