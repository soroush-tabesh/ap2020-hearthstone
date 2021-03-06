package ir.soroushtabesh.hearthstone.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.DeckReaderModel;
import ir.soroushtabesh.hearthstone.models.ScriptModel;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Quest;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import scripts.QuestWatch;
import scripts.Summoner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        List<Card> friendly = fillCardList(model.getFriendlyCardNames());
        List<Card> enemy = fillCardList(model.getEnemyCardNames());

        model.setFriendlyDeckList(friendly);
        model.setEnemyDeckList(enemy);

        model.setFriendlyDeck(list2Deck(friendly));
        model.setEnemyDeck(list2Deck(enemy));

        return model;
    }

    private static List<Card> fillCardList(List<String> cardNames) {
        return cardNames.stream().map(DeckReader::getCardFromName).collect(Collectors.toList());
    }

    private static Deck list2Deck(List<Card> cards) {
        Deck friendly = new Deck();
        Map<Card, Integer> cardsInDeck0 = new HashMap<>();
        cards.forEach(card -> cardsInDeck0.put(card, cardsInDeck0.getOrDefault(card, 0) + 1));
        friendly.setCardsInDeck(cardsInDeck0);
        return friendly;
    }

    private static Deck fillDeck(List<String> cardNames) {
        Deck friendly = new Deck();
        Map<Card, Integer> cardsInDeck0 = new HashMap<>();
        cardNames.forEach(s -> {
            Card cardFromName = getCardFromName(s);
            cardsInDeck0.put(cardFromName, cardsInDeck0.getOrDefault(cardFromName, 0) + 1);
        });
        friendly.setCardsInDeck(cardsInDeck0);
        return friendly;
    }

    private static int cntr = 10000;

    public static Card getCardFromName(String name) {
        name = name.toLowerCase().trim();
        if (name.equals("spell"))
            return getRandomSpell();
        if (name.equals("minion"))
            return getRandomMinion();
        if (name.equals("weapon"))
            return getRandomWeapon();
        String[] split = name.split("->reward:");
        for (int i = 0; i < split.length; i++)
            split[i] = split[i].trim();

        Card card = CardManager.getInstance().getCardByName(split[0]);
        if (card == null)
            throw new NoSuchCardException(name);
        if (split.length == 2) {
            Quest card1 = (Quest) card;
            Quest quest = new Quest(card1.getName(), card1.getDescription(), card1.getMana()
                    , card1.getHeroClass(), card1.getPrice(), card1.getRarity());
            quest.setId(cntr++);
            QuestWatch script = (QuestWatch) card1.getScriptModel().getScript(null);
            script.setCustomReward(() -> new Summoner(split[1]).onSpellEffect(null));
            quest.setScriptModel(new ScriptModel(script));
            card = quest;
        }
        return card;
    }

    public static List<Card> getCardsFromNames(List<String> names) {
        return names.stream().map(DeckReader::getCardFromName).collect(Collectors.toList());
    }

    public static Minion getRandomMinion() {
        List<Minion> cards = CardManager.getInstance().getAllMinions();
        return cards.get(new SecureRandom().nextInt(cards.size()));
    }

    public static Spell getRandomSpell() {
        List<Spell> cards = CardManager.getInstance().getAllSpells();
        return cards.get(new SecureRandom().nextInt(cards.size()));
    }

    private static Weapon getRandomWeapon() {
        List<Weapon> cards = CardManager.getInstance().getAllWeapons();
        return cards.get(new SecureRandom().nextInt(cards.size()));
    }
}
