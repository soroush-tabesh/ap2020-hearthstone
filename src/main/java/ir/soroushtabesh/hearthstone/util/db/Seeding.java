package ir.soroushtabesh.hearthstone.util.db;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.custom.*;
import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.models.cards.*;
import ir.soroushtabesh.hearthstone.util.Constants;

import java.io.File;
import java.util.List;

/**
 * Seeding note:<br>
 * <b>Call <code>save</code> on objects before linking.</b> <br><br>You need their id for
 * association which will be available after <code>save</code>
 */
public class Seeding {


    private Seeding() {
    }

    public static void seed() {
        try {
            //Heroes
            Hero mage = new Hero("Jaina Proudmoore",
                    Hero.HeroClass.MAGE,
                    30,
                    new HeroPower("Fireblast", "", 2
                            , Hero.HeroClass.MAGE, 2, Card.Rarity.FREE));
            Hero warlock = new Hero("Gul'dan",
                    Hero.HeroClass.WARLOCK,
                    35,
                    new HeroPower("Life Tap", "", 2
                            , Hero.HeroClass.WARLOCK, 2, Card.Rarity.FREE));
            Hero rogue = new Hero("Valeera Sanguinar",
                    Hero.HeroClass.ROUGE,
                    30,
                    new HeroPower("Dagger Mastery", "", 2
                            , Hero.HeroClass.ROUGE, 2, Card.Rarity.FREE));
            Hero hunter = new Hero("Alleria Windrunner",
                    Hero.HeroClass.HUNTER,
                    30,
                    new HeroPower("Caltrops", "", 2
                            , Hero.HeroClass.HUNTER, 2, Card.Rarity.FREE));
            Hero paladin = new Hero("Prince Arthas",
                    Hero.HeroClass.PALADIN,
                    30,
                    new HeroPower("The Silver Hand", "", 2
                            , Hero.HeroClass.PALADIN, 2, Card.Rarity.FREE));
            Hero priest = new Hero("Tyrande Whisperwind",
                    Hero.HeroClass.PRIEST,
                    30,
                    new HeroPower("Heal", "", 2
                            , Hero.HeroClass.PRIEST, 2, Card.Rarity.FREE));

            //Special cards
            Spell mage_spec = new Spell("Polymorph",
                    "Transform a minion into a 1/1 Sheep.",
                    4,
                    Hero.HeroClass.MAGE,
                    3,
                    Card.Rarity.COMMON);
            mage_spec.setScriptModel(new ScriptModel(new Transformer("Sheep")));

            Spell mage_spec2 = new Spell("Mirror Image",
                    "Summon two 0/2 minions with Taunt.",
                    1,
                    Hero.HeroClass.MAGE,
                    3,
                    Card.Rarity.COMMON);
            mage_spec2.setScriptModel(new ScriptModel(new Summoner("Peshgel", "Peshgel")));

            Spell rogue_spec = new Spell("Friendly Smith",
                    "Discover a weapon\n" +
                            "from any class. Add it\n" +
                            "to your Deck\n" +
                            "with +2/+2.",
                    1,
                    Hero.HeroClass.ROUGE,
                    3,
                    Card.Rarity.COMMON);
            rogue_spec.setScriptModel(new ScriptModel(new RandomAdderToDeck(new PowerUpMinion(2, 2))));

            Minion rogue_spec2 = new Minion("Skyvateer",
                    "Stealth\n" +
                            "Deathrattle: Draw a card.",
                    2,
                    Hero.HeroClass.ROUGE,
                    2,
                    Card.Rarity.COMMON,
                    3,
                    1,
                    Minion.MinionClass.PIRATE);
            rogue_spec2.setScriptModel(new ScriptModel(new MinionBehaviorList()
                    .add(new Stealth()).add(new Deathrattle(new RandomDrawer()))));

            Minion warlock_spec = new Minion("Dreadscale",
                    "At the end of your turn, deal 1 damage to all other minions.",
                    3,
                    Hero.HeroClass.WARLOCK,
                    12,
                    Card.Rarity.LEGENDARY,
                    2,
                    4,
                    Minion.MinionClass.BEAST);
            warlock_spec.setScriptModel(new ScriptModel(new Deathrattle(new Bomb(1))));

            Minion warlock_spec2 = new Minion("Felguard",
                    "Taunt. Battlecry: Destroy one of your Mana Crystals.",
                    3,
                    Hero.HeroClass.WARLOCK,
                    7,
                    Card.Rarity.RARE,
                    5,
                    3,
                    Minion.MinionClass.DEMON);
            warlock_spec2.setScriptModel(new ScriptModel(new MinionBehaviorList().
                    add(new Taunt()).add(new BattleCry(new ManaPowerUp(0, -1)))));

            Minion hunter_spec = new Minion("Swamp King Dred",
                    "After your opponent plays a minion, attack it.",
                    7,
                    Hero.HeroClass.HUNTER,
                    8,
                    Card.Rarity.LEGENDARY,
                    9,
                    9,
                    Minion.MinionClass.BEAST);
            hunter_spec.setScriptModel(new ScriptModel(new AttackOnPlay()));

            Spell hunter_spec2 = new Spell("Unleash the Hounds",
                    "For each enemy minion, summon a 1/1 Hound with Charge.",
                    3,
                    Hero.HeroClass.HUNTER,
                    3,
                    Card.Rarity.COMMON);
            hunter_spec2.setScriptModel(new ScriptModel(new ForEachEnemyMinion(new Summoner("Hound"))));

            Spell paladin_spec = new Spell("Gnomish Army Knife",
                    "Give a minion Charge, Windfury, Divine Shield, Lifesteal, Poisonous, Taunt, and Stealth.",
                    5,
                    Hero.HeroClass.PALADIN,
                    3,
                    Card.Rarity.FREE);
            paladin_spec.setScriptModel(new ScriptModel(new TargetAdd()
                    .add(new Charge()).add(new Windfury()).add(new DivineShield()).add(new Lifesteal())
                    .add(new Poisonous()).add(new Taunt()).add(new Stealth()))); // :/

            Spell paladin_spec2 = new Spell("Blessing of Might",
                    "Give a minion +3 Attack.",
                    1,
                    Hero.HeroClass.PALADIN,
                    3,
                    Card.Rarity.FREE);
            paladin_spec2.setScriptModel(new ScriptModel(new TargetAdd().add(new PowerUpMinion(0, 3))));

            Minion priest_spec = new Minion("High Priest Amet",
                    "Whenever you summon a minion, set its Health equal to this minion's.",
                    4,
                    Hero.HeroClass.PRIEST,
                    8,
                    Card.Rarity.LEGENDARY,
                    7,
                    2,
                    Minion.MinionClass.ALL);
            priest_spec.setScriptModel(new ScriptModel(new HighPriestAmet()));

            Spell priest_spec2 = new Spell("Holy Ripple",
                    "Deal 1 damage to all enemies. Restore 1 Health to all friendly characters.",
                    2,
                    Hero.HeroClass.PRIEST,
                    3,
                    Card.Rarity.RARE);
            priest_spec2.setScriptModel(new ScriptModel(new MultiSpell()
                    .add(new Bomb(1, false)).add(new RestoreHealthAllMinions(1))));

            //Spells
            Spell spell1 = new Spell("Sprint",
                    "Draw 4 cards.",
                    7,
                    Hero.HeroClass.ALL,
                    7,
                    Card.Rarity.FREE);
            spell1.setScriptModel(new ScriptModel(new MultiSpell()
                    .add(new RandomDrawer()).add(new RandomDrawer()).add(new RandomDrawer()).add(new RandomDrawer())));

            Spell spell2 = new Spell("Swarm of Locusts",
                    "Summon seven 1/1 Locusts with Rush.",
                    6,
                    Hero.HeroClass.ALL,
                    3,
                    Card.Rarity.RARE);
            spell2.setScriptModel(new ScriptModel(new MultiSpell()
                    .add(new Summoner("Locust")).add(new Summoner("Locust"))
                    .add(new Summoner("Locust")).add(new Summoner("Locust"))
                    .add(new Summoner("Locust")).add(new Summoner("Locust"))
                    .add(new Summoner("Locust"))));

            Spell spell3 = new Spell("Pharaoh's Blessing",
                    "Give a minion +4/+4, Divine Shield, and Taunt.",
                    6,
                    Hero.HeroClass.ALL,
                    3,
                    Card.Rarity.RARE);
            spell3.setScriptModel(new ScriptModel(new TargetAdd()
                    .add(new PowerUpMinion(4, 4)).add(new DivineShield()).add(new Taunt())));

            Spell spell4 = new Spell("Book of Specters",
                    "Draw 3 cards. Discard any spells drawn.",
                    2,
                    Hero.HeroClass.ALL,
                    3,
                    Card.Rarity.EPIC);
            spell4.setScriptModel(new ScriptModel(new MultiSpell()
                    .add(new RandomDrawer(false)).add(new RandomDrawer(false))
                    .add(new RandomDrawer(false))));

            Spell spell5 = new Spell("Frostbolt",
                    "Deal 3 damage to a character and Freeze it.",
                    2,
                    Hero.HeroClass.ALL,
                    2,
                    Card.Rarity.COMMON);
            spell5.setScriptModel(new ScriptModel(new TargetAdd()
                    .add(new PowerUpMinion(-3, 0)).add(new Freeze())));

            Spell spell6 = new Spell("Brawl",
                    "Destroy all minions except one. (chosen randomly)",
                    5,
                    Hero.HeroClass.ALL,
                    5,
                    Card.Rarity.EPIC);
            spell6.setScriptModel(new ScriptModel(new Brawl()));

            Spell spell7 = new Spell("Overflow",
                    "Restore 5 Health to all characters. Draw 5 cards.",
                    7,
                    Hero.HeroClass.ALL,
                    5,
                    Card.Rarity.RARE);
            spell7.setScriptModel(new ScriptModel(new MultiSpell()
                    .add(new RestoreHealthAllHeroes())
                    .add(new RandomDrawer()).add(new RandomDrawer()).add(new RandomDrawer()).add(new RandomDrawer())
                    .add(new RandomDrawer())));

            Spell spell8 = new Spell("Mind Control",
                    "Take control of an enemy minion.",
                    10,
                    Hero.HeroClass.ALL,
                    15,
                    Card.Rarity.COMMON);
            spell8.setScriptModel(new ScriptModel(new TakeControl()));

            Spell spell9 = new Spell("Slam",
                    "Deal 2 damage to a minion. If it survives, draw a card.",
                    2,
                    Hero.HeroClass.ALL,
                    2,
                    Card.Rarity.COMMON);
            spell9.setScriptModel(new ScriptModel(new Slam()));

            Spell spell10 = new Spell("Deadly Shot",
                    "Destroy a random enemy minion.",
                    3,
                    Hero.HeroClass.ALL,
                    7,
                    Card.Rarity.COMMON);
            spell10.setScriptModel(new ScriptModel(new DeadlyShot()));

            //Minion
            Minion minion1 = new Minion("Sathrovarr",
                    "Battlecry: Choose a friendly minion. Add a copy of it to your hand, deck and battlefield.",
                    9,
                    Hero.HeroClass.ALL,
                    8,
                    Card.Rarity.LEGENDARY,
                    5,
                    5,
                    Minion.MinionClass.DEMON);
            minion1.setScriptModel(new ScriptModel(new Sathrovarr()));

            Minion minion2 = new Minion("Tomb Warden",
                    "Taunt\n" +
                            "Battlecry: Summon a copy of this minion.",
                    8,
                    Hero.HeroClass.ALL,
                    8,
                    Card.Rarity.RARE,
                    6,
                    3,
                    Minion.MinionClass.MECH);
            minion2.setScriptModel(new ScriptModel(new MinionBehaviorList().add(new Taunt()).add(new TombWarden())));

            Minion minion3 = new Minion("Security Rover",
                    "Whenever this minion takes damage, summon a 2/3 Mech with Taunt.",
                    6,
                    Hero.HeroClass.ALL,
                    10,
                    Card.Rarity.RARE,
                    6,
                    2,
                    Minion.MinionClass.MECH);
            minion3.setScriptModel(new ScriptModel(new OnDamageTaken(new Summoner("Cholagh"))));

            Minion minion4 = new Minion("Curio Collector",
                    "Whenever you draw a card, gain +1/+1.",
                    5,
                    Hero.HeroClass.ALL,
                    8,
                    Card.Rarity.LEGENDARY,
                    4,
                    4,
                    Minion.MinionClass.ALL);
            minion4.setScriptModel(new ScriptModel(new CurioCollector()));

            Minion minion5 = new Minion("Wolfrider",
                    "Charge",
                    3,
                    Hero.HeroClass.ALL,
                    1,
                    Card.Rarity.COMMON,
                    1,
                    3,
                    Minion.MinionClass.ALL);
            minion5.setScriptModel(new ScriptModel(new Charge()));

            Minion minion6 = new Minion("Wisp",
                    "",
                    0,
                    Hero.HeroClass.ALL,
                    1,
                    Card.Rarity.COMMON,
                    1,
                    1,
                    Minion.MinionClass.ALL);

            Minion minion7 = new Minion("Voodoo Doctor",
                    "Battlecry: Restore 2 Health.",
                    2,
                    Hero.HeroClass.ALL,
                    2,
                    Card.Rarity.COMMON,
                    1,
                    2,
                    Minion.MinionClass.ALL);
            minion7.setScriptModel(new ScriptModel(new BattleCry(new RestoreHealthMyHero(2))));

            Minion minion8 = new Minion("Waterboy",
                    "Battlecry: Your next Hero Power this turn costs (0).",
                    2,
                    Hero.HeroClass.ALL,
                    2,
                    Card.Rarity.EPIC,
                    1,
                    2,
                    Minion.MinionClass.ALL);
            minion8.setScriptModel(new ScriptModel(new BattleCry(new FreeHeroPower())));

            Minion minion9 = new Minion("Zilliax",
                    "Magnetic\n" +
                            "Divine Shield, Taunt, Lifesteal, Rush",
                    5,
                    Hero.HeroClass.ALL,
                    2,
                    Card.Rarity.LEGENDARY,
                    2,
                    3,
                    Minion.MinionClass.MECH);
            minion9.setScriptModel(new ScriptModel(new MinionBehaviorList()
                    .add(new DivineShield()).add(new Taunt()).add(new Lifesteal()).add(new Rush())));

            Minion minion10 = new Minion("Baron Geddon",
                    "At the end of your turn, deal 2 damage to ALL other characters.",
                    7,
                    Hero.HeroClass.ALL,
                    2,
                    Card.Rarity.LEGENDARY,
                    5,
                    7,
                    Minion.MinionClass.ELEMENTAL);
            minion10.setScriptModel(new ScriptModel(new OnTurnEnd(new Bomb(2))));

            Minion minion11 = new Minion("Cholagh",
                    "Taunt",
                    0,
                    Hero.HeroClass.WARRIOR,
                    0,
                    Card.Rarity.RARE,
                    3,
                    2,
                    Minion.MinionClass.MECH);
            minion11.setTradable(false);
            minion11.setScriptModel(new ScriptModel(new Taunt()));

            Minion minion12 = new Minion("Locust",
                    "Rush",
                    1,
                    Hero.HeroClass.ALL,
                    0,
                    Card.Rarity.COMMON,
                    1,
                    1,
                    Minion.MinionClass.BEAST);
            minion12.setTradable(false);
            minion12.setScriptModel(new ScriptModel(new Rush()));

            Minion minion13 = new Minion("Peshgel",
                    "Taunt",
                    1,
                    Hero.HeroClass.ALL,
                    0,
                    Card.Rarity.COMMON,
                    2,
                    0,
                    Minion.MinionClass.ALL);
            minion13.setTradable(false);
            minion13.setScriptModel(new ScriptModel(new Taunt()));

            Minion minion14 = new Minion("Sheep",
                    "",
                    1,
                    Hero.HeroClass.ALL,
                    0,
                    Card.Rarity.COMMON,
                    1,
                    1,
                    Minion.MinionClass.BEAST);
            minion14.setTradable(false);
            minion14.setScriptModel(new ScriptModel(new Taunt()));


            //Quests
            Quest quest1 = new Quest("Strength in Numbers",
                    "Sidequest: Spend 10 Mana on minions.\n" +
                            "Reward: Summon a minion from your deck.",
                    1,
                    Hero.HeroClass.ALL,
                    3,
                    Card.Rarity.COMMON);
            quest1.setScriptModel(new ScriptModel(new StrengthInNumbers()));

            Quest quest2 = new Quest("Learn Draconic",
                    "Sidequest: Spend 8 Mana on spells. Reward: Summon a 6/6 Dragon.",
                    1,
                    Hero.HeroClass.ALL,
                    3,
                    Card.Rarity.COMMON);
            quest2.setScriptModel(new ScriptModel(new LearnDraconic()));

            //Weapons
            Weapon weapon1 = new Weapon("Blood Fury",
                    "",
                    3,
                    Hero.HeroClass.ALL,
                    7,
                    Card.Rarity.COMMON,
                    8,
                    3);
            Weapon weapon2 = new Weapon("Battle Axe",
                    "",
                    1,
                    Hero.HeroClass.ALL,
                    1,
                    Card.Rarity.COMMON,
                    2,
                    2);
            Weapon weapon3 = new Weapon("Dragon Claw",
                    "",
                    5,
                    Hero.HeroClass.ALL,
                    5,
                    Card.Rarity.COMMON,
                    2,
                    5);
            Weapon weapon4 = new Weapon("Mirage Blade",
                    "Your hero is Immune while attacking.",
                    2,
                    Hero.HeroClass.ALL,
                    4,
                    Card.Rarity.COMMON,
                    3,
                    2);
            weapon4.setScriptModel(new ScriptModel(new MirageBlade()));

            Weapon weapon5 = new Weapon("Wicked Knife",
                    "",
                    1,
                    Hero.HeroClass.ALL,
                    1,
                    Card.Rarity.COMMON,
                    1,
                    2);

            Weapon weapon6 = new Weapon("Desert Spear",
                    "After your hero attacks, summon a 1/1 Locust with Rush.",
                    3,
                    Hero.HeroClass.ALL,
                    6,
                    Card.Rarity.COMMON,
                    3,
                    1);
            weapon6.setScriptModel(new ScriptModel(new DesertSpear()));


            InfoPassive passive1 = new InfoPassive("Twice Draw", "");
            passive1.setScriptModel(new ScriptModel(new TwiceDraw()));
            InfoPassive passive2 = new InfoPassive("Off Draws", "");
            passive2.setScriptModel(new ScriptModel(new OffDraws()));
            InfoPassive passive3 = new InfoPassive("Warriors", "");
            passive3.setScriptModel(new ScriptModel(new Warriors()));
            InfoPassive passive4 = new InfoPassive("Nurse", "");
            passive4.setScriptModel(new ScriptModel(new Nurse()));
            InfoPassive passive5 = new InfoPassive("Free power", "");
            passive5.setScriptModel(new ScriptModel(new FreePower()));

            DBUtil.pushObjects(
                    mage, warlock, rogue, hunter, priest, paladin
                    , spell1, spell2, spell3, spell4, spell5, spell6, spell7, spell8, spell9, spell10
                    , minion1, minion2, minion3, minion4, minion5, minion6, minion7, minion8, minion9, minion10
                    , minion11, minion12, minion13, minion14
                    , weapon1, weapon2, weapon3, weapon4, weapon5, weapon6
                    , mage_spec, warlock_spec, rogue_spec, hunter_spec, priest_spec, paladin_spec
                    , mage_spec2, warlock_spec2, rogue_spec2, hunter_spec2, priest_spec2, paladin_spec2
                    , quest1, quest2
                    , passive1, passive2, passive3, passive4, passive5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seedPlayer(Player player) {
        DBUtil.doInJPA(session -> {
            List<Hero> heroes = session.createQuery("from Hero ", Hero.class).list();
            player.getOpenHeroes().addAll(heroes);
            List<Card> cards = session
                    .createQuery("from Card where id < 37 and deckAssociative=true", Card.class)
                    .setMaxResults(27).list();
            cards.forEach(player::addOwnedCard);

            //showcase seed
            for (Hero hero : heroes) {
                Deck deck = new Deck(hero.getHeroClass(), player);
                cards.forEach(deck::addCard);

                deck.setName(hero.getName() + " #1");
                deck.getCardsInDeck().addAll(cards);

                int r = (int) (10 * Math.random());

                deck.getDeckHistory().setTotalGames(r);
                deck.getDeckHistory().setWonGames((int) (r * Math.random()));
                cards.forEach(card -> deck.getDeckHistory().getCardsInDeckUsage().put(card, (int) (10 * Math.random())));

                player.getPlayerStats().setGameCount(r);
                player.getPlayerStats().setWinCount((int) (r * Math.random()));

                session.saveOrUpdate(deck);
                player.addDeck(deck);
            }
            return null;
        });
    }

    public static void initiate() {
        File file = new File(Constants.DB_URL + Constants.DB_URL_SUFFIX);
        if (!file.exists())
            seed();
    }
}
