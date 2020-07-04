package ir.soroushtabesh.hearthstone.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.DeckReaderModel;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class DeckReader {

    private DeckReader() {
    }

    public static DeckReaderModel read(File file) {
        try {
            return read(Files.readString(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DeckReaderModel read(String string) {
        Gson gson = new GsonBuilder().create();
        DeckReaderModel model = gson.fromJson(string, DeckReaderModel.class);
        Deck friendly = new Deck();
        model.getFriendlyCardNames().forEach(s -> friendly.addCard(getCardFromName(s)));
        Deck enemy = new Deck();
        model.getFriendlyCardNames().forEach(s -> enemy.addCard(getCardFromName(s)));
        model.setFriendly(friendly);
        model.setEnemy(enemy);
        return model;
    }

    public static Card getCardFromName(String name) {
        if (name.toLowerCase().equals("spell"))
            return getRandomSpell();
        if (name.toLowerCase().equals("minion"))
            return getRandomMinion();
        String[] split = name.split("->");
        Card card = DBUtil.doInJPA(session -> session
                .createQuery("from Card where lower(name)=:name ", Card.class)
                .setParameter("name", split[0])
                .uniqueResult());
        if (split.length == 2) {
            //todo: it's a quest. configure it.
        }
        return card;
    }

    public static List<Card> getCardsFromNames(List<String> names) {
        return names.stream().map(DeckReader::getCardFromName).collect(Collectors.toList());
    }

    public static Hero getHeroByClass(String cls) {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Hero where lower(heroClass)=:cls ", Hero.class)
                .setParameter("cls", cls.toLowerCase())
                .uniqueResult());
    }

    public static Minion getRandomMinion() {
        List<Minion> cards = DBUtil.doInJPA(session -> session
                .createQuery("from Minion ", Minion.class)
                .list());
        return cards.get(new SecureRandom().nextInt(cards.size()));
    }

    public static Spell getRandomSpell() {
        List<Spell> cards = DBUtil.doInJPA(session -> session
                .createQuery("from Spell ", Spell.class)
                .list());
        return cards.get(new SecureRandom().nextInt(cards.size()));
    }
}
