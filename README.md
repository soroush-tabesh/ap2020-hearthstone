# Emulation of Hearthstone game
_Currently under development._ 
## Phase 1 Documentation
This phase implements data models and a modest CLI for the sake of test. CLI supports user sign-up, login session,
 a simple collection management system and a simple store system.
### Libraries and Dependencies
- [Hibernate](http://hibernate.org/orm/) - Robust and widely used ORM, with an active community.
- [H2](https://h2database.com) - Small SQL database, with embedded mode available.
- [Apache Log4j 2](https://logging.apache.org/log4j/) - Taking control of Hibernate logging.
- [Guava](https://github.com/google/guava) - Collections, caching, primitives support, concurrency libraries, common
 annotations, string processing, I/O, and more.
- [Java ASCII Render](https://github.com/indvd00m/java-ascii-render) - Graphical primitives for the console.
- [Apache Commons Codec](http://commons.apache.org/proper/commons-codec/) - General encoding/decoding algorithms, e.g. 
phonetic, base64 or URL.

### CLI
The CLIManager is a Singleton for managing CLIActivities. Each page in CLI is a subclass of CLIActivity abstract.

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

| Diagram |
| ------------- |
| ![](./readme_resources/CLIActivity.png) |

### Command Processor
The CommandProcessor system accepts Observers of commands. You may register ProcessListener associated with a command, to the processor
and then let processor handle the incoming CLI inputs which will be then relayed to the assigned Listener. 

After adding necessary commands, it would suffice to call process method with tokenized input line to get the processor running.

**CLIManager uses special tokenizer which would avoid white space splitting inside quotations.** 
For example `this is -a "test for processor"` will be tokenized to `[this, is, -a, test for processor]`.
As you see quotations marks are ignored after tokenize.

| Diagram |
| ------------- |
| ![](./readme_resources/CommandProcessor.png) |

### Player Manager
This is a Singleton controller for Player model. 