package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.cli.CLIManager;
import ir.soroushtabesh.hearthstone.cli.activities.StartPage;
import ir.soroushtabesh.hearthstone.models.beans.*;
import ir.soroushtabesh.hearthstone.models.beans.cards.HeroPower;
import ir.soroushtabesh.hearthstone.models.beans.cards.Minion;
import ir.soroushtabesh.hearthstone.util.DBUtil;
import ir.soroushtabesh.hearthstone.util.Logger;
import org.hibernate.Session;

public class Application {
    public static void main(String[] args) {
        //initiate();
        //initiate2();
        CLIManager cliManager = CLIManager.getInstance();
        cliManager.startActivity(new StartPage());
        cliManager.fireUp();
        /*
        for deploy:
        1- enable banner
        2- disable sql log
        3- write help
        4- erase debug shit
         */
    }

    public static void initiate() {
        Logger.log("test", "test", Log.Severity.WARNING);
        Player player = new Player("ss", "ss");
        Script script = new Script() {
        };
        HeroPower heroPower = new HeroPower();
        Hero hero = new Hero();
        Deck deck = new Deck(hero, player);
        Minion minion = new Minion();
        player.addDeck(deck);
        player.addOpenHero(hero);
        player.addOwnedCard(minion);
        hero.addSpecialCard(minion);
        hero.setHeroClass(Hero.HeroClass.DRUID);
        hero.setHeroPower(heroPower);
        hero.setHp(40);
        hero.setName("Akbar");
        hero.setSpecialPower(script);
        deck.addCard(minion);
        minion.setCard_name("Ghozmit");
        minion.setAttackPower(2);
        minion.setHp(5);
        minion.setMinionClass(Minion.MinionClass.BEAST);
        minion.setDescription("Khafan");
        minion.setHeroClass(hero.getHeroClass());
        minion.setMana(3);
        minion.setScript(script);
        DBUtil.syncSingleObject(player);
    }

    public static void initiate2() {
        try (Session session = DBUtil.openSession()) {
            session.beginTransaction();
            Player player = (Player) session.createQuery("from Player where username=:username ")
                    .setParameter("username", "ss").uniqueResult();
            player.setPassword("chos");
            session.saveOrUpdate(player);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
