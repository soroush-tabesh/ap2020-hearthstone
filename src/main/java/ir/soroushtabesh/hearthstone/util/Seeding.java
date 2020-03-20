package ir.soroushtabesh.hearthstone.util;

import ir.soroushtabesh.hearthstone.models.beans.*;
import ir.soroushtabesh.hearthstone.models.beans.cards.Minion;
import ir.soroushtabesh.hearthstone.models.beans.cards.Quest;
import ir.soroushtabesh.hearthstone.models.beans.cards.Spell;
import ir.soroushtabesh.hearthstone.models.beans.cards.Weapon;
import ir.soroushtabesh.hearthstone.models.beans.scripts.Dummy;
import ir.soroushtabesh.hearthstone.models.beans.scripts.HeroPower;
import org.hibernate.Session;

import java.io.File;
import java.util.List;

public class Seeding {
    public static void initiate1() {
        Logger.log("test", "test", Log.Severity.WARNING);
        try (Session session = DBUtil.openSession()) {
            Player player = new Player("akbar", HashUtil.hash("akbar"));
            Script script = new Dummy();
            HeroPower heroPower = new HeroPower();
            Hero hero = new Hero();
            Deck deck = new Deck(hero, player);
            Minion minion = new Minion();
            Minion minion1 = new Minion();

            DBUtil.pushSingleObject(player, session);
            DBUtil.pushSingleObject(script, session);
            DBUtil.pushSingleObject(heroPower, session);
            DBUtil.pushSingleObject(hero, session);
            DBUtil.pushSingleObject(deck, session);
            DBUtil.pushSingleObject(minion, session);
            DBUtil.pushSingleObject(minion1, session);

            player.addDeck(deck);
            player.addOpenHero(hero);
            player.addOwnedCard(minion);
            player.addOwnedCard(minion1);

            hero.addSpecialCard(minion);
            hero.setHeroClass(Hero.HeroClass.DRUID);
            hero.setHeroPower(heroPower);
            hero.setHp(40);
            hero.setName("Akbar");
            hero.setSpecialPower(script);

            deck.addCard(minion);
            deck.addCard(minion1);

            minion.setCard_name("Ghozmit");
            minion.setAttackPower(2);
            minion.setHp(5);
            minion.setMinionClass(Minion.MinionClass.BEAST);
            minion.setDescription("Khafan");
            minion.setHeroClass(hero.getHeroClass());
            minion.setMana(3);
            minion.setPrice(12);
            minion.setRarity(Card.Rarity.RARE);
            minion.setScript(script);

            minion1.setCard_name("Peshgel");
            minion1.setAttackPower(3);
            minion1.setHp(1);
            minion1.setMinionClass(Minion.MinionClass.BEAST);
            minion1.setDescription("Oskol");
            minion1.setHeroClass(hero.getHeroClass());
            minion1.setMana(1);
            minion1.setPrice(3);
            minion1.setRarity(Card.Rarity.EPIC);
            minion1.setScript(script);

            DBUtil.pushSingleObject(player, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seed() {
        /*
        what do we need?
        3x Hero: mage, warlock, rouge
        3x Card special
        6x Card spell
        6x Card minion
        3x Card quest
        3x Card weapon special

        script
        heropower
        hero
        deck
        minion
        */
        try (Session session = DBUtil.openSession()) {

            Hero mage = new Hero();
            Hero warlock = new Hero();
            Hero rogue = new Hero();

            HeroPower heroPower_mage = new HeroPower();
            HeroPower heroPower_warlock = new HeroPower();
            HeroPower heroPower_rogue = new HeroPower();

            Spell mage_spec_morph = new Spell();
            Minion warlock_spec_dread = new Minion();
            Spell rogue_spec_smith = new Spell();

            Spell spell1 = new Spell();
            Spell spell2 = new Spell();
            Spell spell3 = new Spell();
            Spell spell4 = new Spell();

            Minion minion1 = new Minion();
            Minion minion2 = new Minion();
            Minion minion3 = new Minion();
            Minion minion4 = new Minion();
            Minion minion5 = new Minion();
            Minion minion6 = new Minion();
            Minion minion7 = new Minion();
            Minion minion8 = new Minion();

            Quest quest1 = new Quest();
            Quest quest2 = new Quest();
            Quest quest3 = new Quest();

            Weapon weapon1 = new Weapon();
            Weapon weapon2 = new Weapon();
            Weapon weapon3 = new Weapon();

            DBUtil.pushSingleObject(mage, session);
            DBUtil.pushSingleObject(warlock, session);
            DBUtil.pushSingleObject(rogue, session);

            DBUtil.pushSingleObject(heroPower_mage, session);
            DBUtil.pushSingleObject(heroPower_rogue, session);
            DBUtil.pushSingleObject(heroPower_warlock, session);

            DBUtil.pushSingleObject(mage_spec_morph, session);
            DBUtil.pushSingleObject(warlock_spec_dread, session);
            DBUtil.pushSingleObject(rogue_spec_smith, session);

            DBUtil.pushSingleObject(spell1, session);
            DBUtil.pushSingleObject(spell2, session);
            DBUtil.pushSingleObject(spell3, session);
            DBUtil.pushSingleObject(spell4, session);

            DBUtil.pushSingleObject(minion1, session);
            DBUtil.pushSingleObject(minion2, session);
            DBUtil.pushSingleObject(minion3, session);
            DBUtil.pushSingleObject(minion4, session);
            DBUtil.pushSingleObject(minion5, session);
            DBUtil.pushSingleObject(minion6, session);
            DBUtil.pushSingleObject(minion7, session);
            DBUtil.pushSingleObject(minion8, session);

            DBUtil.pushSingleObject(quest1, session);
            DBUtil.pushSingleObject(quest2, session);
            DBUtil.pushSingleObject(quest3, session);

            DBUtil.pushSingleObject(weapon1, session);
            DBUtil.pushSingleObject(weapon2, session);
            DBUtil.pushSingleObject(weapon3, session);

            mage.setName("Chaghal");
            mage.setHp(30);
            mage.setHeroPower(heroPower_mage);
            mage.getSpecialCards().add(mage_spec_morph);
            mage.getSpecialCards().add(weapon1);
            mage.setHeroClass(Hero.HeroClass.MAGE);

            warlock.setName("Cholagh");
            warlock.setHp(35);
            warlock.setHeroPower(heroPower_warlock);
            warlock.getSpecialCards().add(warlock_spec_dread);
            warlock.getSpecialCards().add(weapon2);
            warlock.setHeroClass(Hero.HeroClass.WARLOCK);

            rogue.setName("Kafbor");
            rogue.setHp(30);
            rogue.setHeroPower(heroPower_rogue);
            rogue.getSpecialCards().add(rogue_spec_smith);
            rogue.getSpecialCards().add(weapon3);
            rogue.setHeroClass(Hero.HeroClass.ROUGE);

            mage_spec_morph.setCard_name("Polymorph");
            mage_spec_morph.setDescription("Transform a minion into a 1/1 Sheep");
            mage_spec_morph.setHeroClass(Hero.HeroClass.MAGE);
            mage_spec_morph.setMana(4);
            mage_spec_morph.setPrice(5);

            warlock_spec_dread.setCard_name("Dreadscale");
            warlock_spec_dread.setDescription("At the end of the turn deal 1 damage to all other minions");
            warlock_spec_dread.setHeroClass(Hero.HeroClass.WARLOCK);
            warlock_spec_dread.setMana(3);
            warlock_spec_dread.setPrice(4);
            warlock_spec_dread.setMinionClass(Minion.MinionClass.BEAST);
            warlock_spec_dread.setHp(2);
            warlock_spec_dread.setAttackPower(4);

            rogue_spec_smith.setCard_name("Friendly Smith");
            rogue_spec_smith.setDescription("Discover a weapon from any class. Add it to your adventure deck with +2/+2");
            rogue_spec_smith.setHeroClass(Hero.HeroClass.ROUGE);
            rogue_spec_smith.setMana(1);
            rogue_spec_smith.setPrice(2);

            spell1.setCard_name("Daneshgah");
            spell1.setDescription("Transforms a minion into a 0/5 Daneshjoo with Taunt.");
            spell1.setMana(2);
            spell1.setPrice(6);

            spell2.setCard_name("Corona");
            spell2.setDescription("Deal 1 damage to all minions.");
            spell2.setMana(3);
            spell2.setPrice(4);
            spell2.setRarity(Card.Rarity.RARE);

            spell3.setCard_name("Ghati");
            spell3.setDescription("Give a beast +3/+3 and shuffle your deck with 3 copy of it");
            spell3.setMana(3);
            spell3.setPrice(6);

            spell4.setCard_name("Snipe");
            spell4.setDescription("Secret: Deal 5 damage to the first played minion by your opponent.");
            spell4.setMana(2);
            spell4.setPrice(6);
            spell4.setRarity(Card.Rarity.EPIC);

            minion1.setCard_name("Daneshjoo");
            minion1.setDescription("Taunt");
            minion1.setMana(3);
            minion1.setPrice(5);
            minion1.setMinionClass(Minion.MinionClass.MURLOC);
            minion1.setHp(5);
            minion1.setAttackPower(0);

            minion2.setCard_name("TA");
            minion2.setDescription("Charge");
            minion2.setMana(1);
            minion2.setPrice(0);
            minion2.setMinionClass(Minion.MinionClass.BEAST);
            minion2.setHp(1);
            minion2.setAttackPower(1);

            minion3.setCard_name("Ostad");
            minion3.setDescription("Summon 3 TA. Divine Shield");
            minion3.setMana(7);
            minion3.setPrice(9);
            minion3.setMinionClass(Minion.MinionClass.DRAGON);
            minion3.setHp(0);
            minion3.setAttackPower(0);

            minion4.setCard_name("Ghozmit");
            minion4.setMana(2);
            minion4.setPrice(2);
            minion4.setMinionClass(Minion.MinionClass.DEMON);
            minion4.setHp(2);
            minion4.setAttackPower(2);

            minion5.setCard_name("Peshgel");
            minion5.setMana(0);
            minion5.setPrice(0);
            minion5.setMinionClass(Minion.MinionClass.GENERAL);
            minion5.setHp(1);
            minion5.setAttackPower(1);

            minion6.setCard_name("Kabaramadala");
            minion6.setMana(9);
            minion6.setPrice(10);
            minion6.setMinionClass(Minion.MinionClass.MURLOC);
            minion6.setHp(8);
            minion6.setAttackPower(8);

            minion7.setCard_name("Palang");
            minion7.setMana(3);
            minion7.setPrice(2);
            minion7.setMinionClass(Minion.MinionClass.MURLOC);
            minion7.setHp(4);
            minion7.setAttackPower(2);

            minion8.setCard_name("Pedar Sag");
            minion8.setDescription("Deathrattle: Deal 2 damage to enemy hero");
            minion8.setMana(1);
            minion8.setPrice(0);
            minion8.setMinionClass(Minion.MinionClass.GENERAL);
            minion8.setHp(1);
            minion8.setAttackPower(1);

            quest1.setCard_name("Chert");
            quest1.setDescription("Boro donbale nokhod sia");

            quest2.setCard_name("Chert");
            quest2.setDescription("Naro donbale nokhod sia");

            quest3.setCard_name("Chert");
            quest3.setDescription("Bia donbale nokhod sia");

            weapon1.setCard_name("Desert Spear");
            weapon1.setDescription("After attack, summon a Pesghel with Rush.");
            weapon1.setAttackPower(1);
            weapon1.setDurability(3);
            weapon1.setPrice(3);
            weapon1.setMana(2);
            weapon1.setHeroClass(Hero.HeroClass.ROUGE);
            weapon1.setRarity(Card.Rarity.RARE);

            weapon2.setCard_name("Chomagh");
            weapon2.setDescription("");
            weapon2.setAttackPower(3);
            weapon2.setDurability(2);
            weapon2.setPrice(3);
            weapon2.setMana(3);
            weapon2.setHeroClass(Hero.HeroClass.WARLOCK);

            weapon3.setCard_name("Tof");
            weapon3.setDescription("After attack, summon a Pesghel with Rush.");
            weapon3.setAttackPower(1);
            weapon3.setDurability(1);
            weapon3.setPrice(1);
            weapon3.setMana(1);

            DBUtil.pushSingleObject(mage, session);
            DBUtil.pushSingleObject(warlock, session);
            DBUtil.pushSingleObject(rogue, session);

            DBUtil.pushSingleObject(mage_spec_morph, session);
            DBUtil.pushSingleObject(warlock_spec_dread, session);
            DBUtil.pushSingleObject(rogue_spec_smith, session);

            DBUtil.pushSingleObject(spell1, session);
            DBUtil.pushSingleObject(spell2, session);
            DBUtil.pushSingleObject(spell3, session);
            DBUtil.pushSingleObject(spell4, session);

            DBUtil.pushSingleObject(minion1, session);
            DBUtil.pushSingleObject(minion2, session);
            DBUtil.pushSingleObject(minion3, session);
            DBUtil.pushSingleObject(minion4, session);
            DBUtil.pushSingleObject(minion5, session);
            DBUtil.pushSingleObject(minion6, session);
            DBUtil.pushSingleObject(minion7, session);
            DBUtil.pushSingleObject(minion8, session);

            DBUtil.pushSingleObject(quest1, session);
            DBUtil.pushSingleObject(quest2, session);
            DBUtil.pushSingleObject(quest3, session);

            DBUtil.pushSingleObject(weapon1, session);
            DBUtil.pushSingleObject(weapon2, session);
            DBUtil.pushSingleObject(weapon3, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seedPlayer(Player player) {
        try (Session session = DBUtil.openSession()) {
            session.refresh(player);
            List<Hero> heroes = session.createQuery("from Hero ", Hero.class).list();
            Hero mage = heroes.get(0);
            for (Hero hero : heroes) {
                Deck deck = new Deck(hero, player);
                DBUtil.pushSingleObject(deck, session);
            }
            List<Card> cards = session.createQuery("from Card where card_id<13", Card.class).list();
            player.getOpenHeroes().add(mage);
            player.getOwnedCards().addAll(cards);
            DBUtil.pushSingleObject(player, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initiate() {
        File file = new File("./gamedata.mv.db");
        if (!file.exists())
            seed();
    }
}
