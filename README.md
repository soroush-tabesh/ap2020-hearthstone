# Emulation of Hearthstone game
_Currently under development._ 

## How to run the application
In the source package(ir.soroushtabesh.hearthstone) there's an Application class which you only need to run
main method. Database system will initialize itself and there's no need for extra work.

## Phase 1 Documentation
This phase implements data models and a modest CLI for the sake of test. CLI supports user sign-up, login session,
 a simple collection management system and a simple store system.
### Libraries and Dependencies
Java 13 is used for build.
Project is built using Gradle.

- [Hibernate](http://hibernate.org/orm/) - Robust and widely used ORM, with an active community.
- [H2](https://h2database.com) - Small SQL database, with embedded mode available.
- [Apache Log4j 2](https://logging.apache.org/log4j/) - Taking control of Hibernate logging.
- [Guava](https://github.com/google/guava) - Collections, caching, primitives support, concurrency libraries, common
 annotations, string processing, I/O, and more.
- [Java ASCII Render](https://github.com/indvd00m/java-ascii-render) - Graphical primitives for the console.
- [Apache Commons Codec](http://commons.apache.org/proper/commons-codec/) - General encoding/decoding algorithms, e.g. 
phonetic, base64 or URL.

### CLI
CLIManager is a Singleton class for managing CLIActivities. Each page in CLI is a subclass of CLIActivity abstract.

An Activity follows several design patterns;
CLIActivity is an Observer to CLIEvents handled by CLIManager. CLIActivity also follows Command pattern; The CLIManager 
calls commands in an Activity, based on the lifecycle of Activities. It is also an Adapter to manage unhandled events 
by its subclasses. The latter events are commands or observations by CLIManager.

Commands of an Activity are:
- onStart
- onResume
- onPause
- onStop
- onCommandReceived

Observations are handled by CommandProcessor system and then passed to onCommandReceived. Hence 
onCommandReceived becomes an indirect observer of commands.

| CLI Class Diagram |
| ------------- |
| ![](./readme_resources/CLIActivity.png) |

#### CLI Help
At any time in the app (except in case of special input like username or password) you may call `hearthstone --help` to get a guide to the current state of CLI.
An additional universal help will be also shown.

### Command Processor
The CommandProcessor system accepts Observers of commands. You may register ProcessListener associated with a command, to the processor
and then let processor handle the incoming CLI inputs which will be then relayed to the assigned Listener. 

After adding necessary commands, it would suffice to call process method with tokenized input line to get the processor running.

**CLIManager uses special tokenizer which would avoid white space splitting inside quotations.** 
For example `this is -a "test for processor"` will be tokenized to `[this, is, -a, test for processor]`.
As you see quotations marks are ignored after tokenize.

| Command Processor Class Diagram |
| ------------- |
| ![](./readme_resources/CommandProcessor.png) |

### Player Management
PlayerManager is a Singleton controller for Player model. PlayerManager takes responsibility for creating and deleting accounts,
managing login session for players and passing Player singleton when logged in.

### DB Utilities
An Extract of widely used methods to communicate with hibernate. Also manages singletons for db sessions and registries.

### Database Seeding
A Seeder class is also included to prepare the default state of database.

### Logging
A Logger class is also included in the project to record everything in database.
A log consists of event and it's description alongside player session , timestamp and severity level.

### Data Models; The BIG BOSS!
The models are:
- Player
- Hero
- Deck
- Card
    - Minion
    - Quest
    - Spell
    - Weapon
- Script
    - Dummy
    - HeroPower
- Log

In addition, modeling of the data, needed adding of extra tables to preserve the relations:

| Models UML |
| ------------- |
| ![](./readme_resources/Dummy.png) |

| Database relations |
| ------------- |
| ![](./readme_resources/DB.png) |

