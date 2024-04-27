
# Exchange Rate

## Setup and Running the Server

Before running the server, ensure that all dependencies are installed and the database is set up properly.

### Installation

1. Install the required packages from `requirements.txt`:

   ```bash
   pip install -r requirements.txt
   ```

### Database Setup

To set up and initialize the database, run the following commands:

```bash
flask db init
flask db migrate
flask db upgrade
```

### Running the Server

To run the server on your local machine, use the following command:

```bash
flask run --host=0.0.0.0 --debug --port=5000
```

### Accessing the Deployed Server

The backend has been deployed on a local server accessible via:

[https://jason.hydra-polaris.ts.net](https://jason.hydra-polaris.ts.net)

*Note: The server can be turned on upon request as it is typically kept offline to protect local ports from public access.*

## API Routes and Functionalities

This API consists of multiple endpoints grouped by their functionality, including user management, transaction handling, group interactions, and statistical data retrieval. Here are some examples for the routes/endpoints:

### User Management

#### Create User

- **Endpoint:** `POST /user`
- **Description:** Registers a new user with a username and password.
- **Body:**
  ```json
  {
    "user_name": "username",
    "password": "password"
  }
  ```

#### Authenticate User

- **Endpoint:** `POST /authentication`
- **Description:** Authenticates user and returns a JWT for accessing protected routes.

### Transaction Handling

#### Create Transaction

- **Endpoint:** `POST /transaction`
- **Description:** Creates a new transaction recording the exchange of currencies between USD and LBP.
- **Body:**
  ```json
  {
    "usd_amount": 100,
    "lbp_amount": 152000,
    "usd_to_lbp": true
  }
  ```

#### Get User Transactions

- **Endpoint:** `GET /transaction`
- **Description:** Retrieves all transactions associated with the authenticated user's ID.

### Group Management

#### Create Group

- **Endpoint:** `POST /group`
- **Description:** Creates a new group with a specified name.

#### Send Group Message

- **Endpoint:** `POST /group/{group_name}/message`
- **Description:** Sends a message to all members of a specified group.

### Statistics

#### Number of Transactions

- **Endpoint:** `GET /number_of_transactions`
- **Description:** Provides the number of transactions over a specified period, aggregated by a specified granularity.

### Exchange Rate Predictions

#### Predict Exchange Rate

- **Endpoint:** `GET /predictRate`
- **Description:** Predicts the exchange rate for a specified future date based on historical data.

## OpenAPi v3.1

Find the OpenAPI documentation inside of the backend folder.


# Project Tree
```
ExchangeRate-7
├─ Android
│  └─ exchange-android-EricNJ
│     ├─ .gitignore
│     ├─ app
│     │  ├─ .gitignore
│     │  ├─ build.gradle
│     │  ├─ proguard-rules.pro
│     │  └─ src
│     │     ├─ androidTest
│     │     │  └─ java
│     │     │     └─ com
│     │     │        └─ example
│     │     │           └─ currencyexchange
│     │     │              └─ ExampleInstrumentedTest.kt
│     │     ├─ main
│     │     │  ├─ AndroidManifest.xml
│     │     │  ├─ java
│     │     │  │  └─ com
│     │     │  │     └─ example
│     │     │  │        └─ currencyexchange
│     │     │  │           ├─ Calculator.kt
│     │     │  │           ├─ ChartFragment.kt
│     │     │  │           ├─ ExchangeFragment.kt
│     │     │  │           ├─ LoginActivity.kt
│     │     │  │           ├─ MainActivity.kt
│     │     │  │           ├─ Prediction.kt
│     │     │  │           ├─ RegistrationActivity.kt
│     │     │  │           ├─ TabsPagerAdapter.kt
│     │     │  │           ├─ TransactionsFragment.kt
│     │     │  │           ├─ api
│     │     │  │           │  ├─ Authentication.kt
│     │     │  │           │  ├─ model
│     │     │  │           │  │  ├─ ExchangeRates.kt
│     │     │  │           │  │  ├─ Group.kt
│     │     │  │           │  │  ├─ GroupMessage.kt
│     │     │  │           │  │  ├─ Message.kt
│     │     │  │           │  │  ├─ Offer.kt
│     │     │  │           │  │  ├─ Token.kt
│     │     │  │           │  │  ├─ Transaction.kt
│     │     │  │           │  │  └─ User.kt
│     │     │  │           │  └─ retrofit.kt
│     │     │  │           ├─ chatting
│     │     │  │           │  ├─ ChatsActivity.kt
│     │     │  │           │  ├─ ChatsFragment.kt
│     │     │  │           │  ├─ Convo.kt
│     │     │  │           │  ├─ GroupConvo.kt
│     │     │  │           │  ├─ GroupMessageAdapter.kt
│     │     │  │           │  ├─ GroupsFragment.kt
│     │     │  │           │  ├─ GroupsRVAdapter.kt
│     │     │  │           │  ├─ MessageAdapter.kt
│     │     │  │           │  └─ UsersRVAdapter.kt
│     │     │  │           ├─ data
│     │     │  │           │  ├─ FutureRate.kt
│     │     │  │           │  ├─ GroupWithLastMessage.kt
│     │     │  │           │  └─ UserWithLastMessage.kt
│     │     │  │           ├─ stats.kt
│     │     │  │           └─ trading
│     │     │  │              ├─ ActiveOffers.kt
│     │     │  │              ├─ Myacceptedoffers.kt
│     │     │  │              └─ TradingFragment.kt
│     │     │  └─ res
│     │     │     ├─ drawable
│     │     │     │  ├─ baseline_add_24.xml
│     │     │     │  ├─ ic_add.xml
│     │     │     │  ├─ ic_back.xml
│     │     │     │  ├─ ic_chart.xml
│     │     │     │  ├─ ic_home_.xml
│     │     │     │  ├─ ic_launcher_background.xml
│     │     │     │  ├─ ic_received_message.xml
│     │     │     │  ├─ ic_refresh.xml
│     │     │     │  ├─ ic_send.xml
│     │     │     │  ├─ ic_sent_message.xml
│     │     │     │  ├─ ic_tradingplat.xml
│     │     │     │  ├─ ic_transactions.xml
│     │     │     │  └─ ic_user.xml
│     │     │     ├─ drawable-v24
│     │     │     │  └─ ic_launcher_foreground.xml
│     │     │     ├─ layout
│     │     │     │  ├─ activity_active_offers.xml
│     │     │     │  ├─ activity_calculator.xml
│     │     │     │  ├─ activity_chats.xml
│     │     │     │  ├─ activity_convo.xml
│     │     │     │  ├─ activity_group_convo.xml
│     │     │     │  ├─ activity_login.xml
│     │     │     │  ├─ activity_main.xml
│     │     │     │  ├─ activity_myoffers.xml
│     │     │     │  ├─ activity_prediction.xml
│     │     │     │  ├─ activity_registration.xml
│     │     │     │  ├─ activity_stats.xml
│     │     │     │  ├─ dialog_creategroup.xml
│     │     │     │  ├─ dialog_trading.xml
│     │     │     │  ├─ dialog_transaction.xml
│     │     │     │  ├─ fragment_chart.xml
│     │     │     │  ├─ fragment_chats.xml
│     │     │     │  ├─ fragment_exchange.xml
│     │     │     │  ├─ fragment_groups.xml
│     │     │     │  ├─ fragment_trading.xml
│     │     │     │  ├─ fragment_transactions.xml
│     │     │     │  ├─ item_receivedmsg.xml
│     │     │     │  ├─ item_recieved_group.xml
│     │     │     │  ├─ item_sentmsg.xml
│     │     │     │  ├─ item_trading.xml
│     │     │     │  ├─ item_transaction.xml
│     │     │     │  └─ item_users.xml
│     │     │     ├─ layout-land
│     │     │     │  ├─ fragment_exchange.xml
│     │     │     │  └─ fragment_trading.xml
│     │     │     ├─ menu
│     │     │     │  ├─ bottom_nav.xml
│     │     │     │  ├─ menu_logged_in.xml
│     │     │     │  └─ menu_logged_out.xml
│     │     │     ├─ mipmap-anydpi-v26
│     │     │     │  ├─ ic_launcher.xml
│     │     │     │  └─ ic_launcher_round.xml
│     │     │     ├─ mipmap-hdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ mipmap-mdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ mipmap-xhdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ mipmap-xxhdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ mipmap-xxxhdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ navigation
│     │     │     │  └─ nav_graph.xml
│     │     │     ├─ values
│     │     │     │  ├─ colors.xml
│     │     │     │  ├─ dimens.xml
│     │     │     │  ├─ strings.xml
│     │     │     │  └─ themes.xml
│     │     │     ├─ values-night
│     │     │     │  └─ themes.xml
│     │     │     ├─ values-w1240dp
│     │     │     │  └─ dimens.xml
│     │     │     ├─ values-w600dp
│     │     │     │  └─ dimens.xml
│     │     │     └─ xml
│     │     │        ├─ backup_rules.xml
│     │     │        └─ data_extraction_rules.xml
│     │     └─ test
│     │        └─ java
│     │           └─ com
│     │              └─ example
│     │                 └─ currencyexchange
│     │                    └─ ExampleUnitTest.kt
│     ├─ build.gradle
│     ├─ gradle
│     │  ├─ build.gradle
│     │  └─ wrapper
│     │     ├─ gradle-wrapper.jar
│     │     └─ gradle-wrapper.properties
│     ├─ gradle.properties
│     ├─ gradlew
│     ├─ gradlew.bat
│     ├─ settings.gradle
│     └─ temp.txt
├─ Backend
│  ├─ NeuralNetwork
│  │  ├─ Models
│  │  │  ├─ FinalMODELLLL.h5
│  │  │  └─ finalMODEL.keras
│  │  ├─ Plot
│  │  │  └─ plotdata.py
│  │  ├─ Predicted
│  │  │  └─ predicted_exchange_rates.csv
│  │  └─ Train
│  │     └─ lastPre.py
│  ├─ __init__.py
│  ├─ app.py
│  ├─ config.py
│  ├─ models
│  │  ├─ ExchangeRate.py
│  │  ├─ Group.py
│  │  ├─ Message.py
│  │  ├─ Offer.py
│  │  ├─ Transaction.py
│  │  ├─ User.py
│  │  └─ __init__.py
│  ├─ requirements.txt
│  ├─ routes
│  │  ├─ __init__.py
│  │  ├─ acceptedOfferRoute.py
│  │  ├─ authenticationRoute.py
│  │  ├─ chatRoute.py
│  │  ├─ exchangeRateRoute.py
│  │  ├─ groupRoute.py
│  │  ├─ offersRoute.py
│  │  ├─ predictedExchangeRate.py
│  │  ├─ statistics.py
│  │  ├─ transactionRoute.py
│  │  ├─ userRoute.py
│  │  └─ usernameRoute.py
│  ├─ schemas
│  │  ├─ __init__.py
│  │  ├─ groupSchema.py
│  │  ├─ messageSchema.py
│  │  ├─ offerSchema.py
│  │  ├─ transactionSchema.py
│  │  └─ userSchema.py
│  └─ utils
│     ├─ __init__.py
│     └─ util.py
├─ Desktop
│  ├─ .idea
│  │  ├─ .gitignore
│  │  ├─ Desktop-App.iml
│  │  ├─ misc.xml
│  │  ├─ modules.xml
│  │  └─ vcs.xml
│  └─ exchange
│     ├─ .gitignore
│     ├─ .idea
│     │  ├─ .gitignore
│     │  ├─ .gitignore.old
│     │  ├─ encodings.xml
│     │  ├─ inspectionProfiles
│     │  │  └─ Project_Default.xml
│     │  ├─ misc.xml
│     │  ├─ misc.xml.old
│     │  ├─ uiDesigner.xml
│     │  ├─ vcs.xml
│     │  └─ vcs.xml.old
│     ├─ .mvn
│     │  └─ wrapper
│     │     ├─ maven-wrapper.jar
│     │     └─ maven-wrapper.properties
│     ├─ lib
│     │  ├─ converter-gson-2.9.0-javadoc.jar
│     │  ├─ converter-gson-2.9.0.jar
│     │  ├─ gson-2.8.5-javadoc.jar
│     │  ├─ gson-2.8.5.jar
│     │  ├─ json-20240303-javadoc.jar
│     │  ├─ json-20240303.jar
│     │  ├─ okhttp-3.14.9-javadoc.jar
│     │  ├─ okhttp-3.14.9.jar
│     │  ├─ okio-1.17.2-javadoc.jar
│     │  ├─ okio-1.17.2.jar
│     │  ├─ retrofit-2.9.0-javadoc.jar
│     │  └─ retrofit-2.9.0.jar
│     ├─ mvnw
│     ├─ mvnw.cmd
│     ├─ pom.xml
│     └─ src
│        └─ main
│           ├─ java
│           │  ├─ com
│           │  │  └─ kjb04
│           │  │     └─ exchange
│           │  │        ├─ Alerts.java
│           │  │        ├─ Authentication.java
│           │  │        ├─ DateParser.java
│           │  │        ├─ Main.java
│           │  │        ├─ OnPageCompleteListener.java
│           │  │        ├─ PageCompleter.java
│           │  │        ├─ Parent.java
│           │  │        ├─ api
│           │  │        │  ├─ Exchange.java
│           │  │        │  ├─ ExchangeService.java
│           │  │        │  └─ model
│           │  │        │     ├─ ExchangeRates.java
│           │  │        │     ├─ FutureRate.java
│           │  │        │     ├─ GroupMessage.java
│           │  │        │     ├─ Message.java
│           │  │        │     ├─ Offer.java
│           │  │        │     ├─ Token.java
│           │  │        │     ├─ Transaction.java
│           │  │        │     └─ User.java
│           │  │        ├─ chat
│           │  │        │  └─ Chat.java
│           │  │        ├─ graph
│           │  │        │  └─ Graph.java
│           │  │        ├─ login
│           │  │        │  └─ Login.java
│           │  │        ├─ predictor
│           │  │        │  └─ Predictor.java
│           │  │        ├─ rates
│           │  │        │  └─ Rates.java
│           │  │        ├─ register
│           │  │        │  └─ Register.java
│           │  │        ├─ statistics
│           │  │        │  └─ Statistics.java
│           │  │        ├─ trading
│           │  │        │  ├─ Trading.java
│           │  │        │  ├─ tradingCreate
│           │  │        │  │  └─ TradingCreate.java
│           │  │        │  └─ tradingOffers
│           │  │        │     └─ TradingOffers.java
│           │  │        └─ transactions
│           │  │           └─ Transactions.java
│           │  └─ module-info.java
│           └─ resources
│              └─ com
│                 └─ kjb04
│                    └─ exchange
│                       ├─ chat
│                       │  ├─ chat.css
│                       │  └─ chat.fxml
│                       ├─ graph
│                       │  ├─ graph.css
│                       │  └─ graph.fxml
│                       ├─ login
│                       │  ├─ login.css
│                       │  └─ login.fxml
│                       ├─ parent.css
│                       ├─ parent.fxml
│                       ├─ predictor
│                       │  ├─ predictor.css
│                       │  └─ predictor.fxml
│                       ├─ rates
│                       │  ├─ rates.css
│                       │  └─ rates.fxml
│                       ├─ register
│                       │  ├─ register.css
│                       │  └─ register.fxml
│                       ├─ statistics
│                       │  ├─ statistics.css
│                       │  └─ statistics.fxml
│                       ├─ trading
│                       │  ├─ trading.css
│                       │  ├─ trading.fxml
│                       │  ├─ tradingCreate
│                       │  │  ├─ tradingCreate.css
│                       │  │  └─ tradingCreate.fxml
│                       │  └─ tradingOffers
│                       │     ├─ tradingOffers.css
│                       │     └─ tradingOffers.fxml
│                       └─ transactions
│                          ├─ transactions.css
│                          └─ transactions.fxml
├─ FrontEnd
│  ├─ README.md
│  ├─ package-lock.json
│  ├─ package.json
│  ├─ public
│  │  ├─ favicon.ico
│  │  ├─ index.html
│  │  ├─ logo192.png
│  │  ├─ logo512.png
│  │  ├─ manifest.json
│  │  └─ robots.txt
│  └─ src
│     ├─ App.css
│     ├─ App.js
│     ├─ App.test.js
│     ├─ AvailibleOffers.js
│     ├─ ChatBox.css
│     ├─ ChatDrawer.css
│     ├─ ChatDrawer.js
│     ├─ ChatGroup.js
│     ├─ ChatUser.css
│     ├─ ChatUser.js
│     ├─ CreateOffer.js
│     ├─ DrawerNav.js
│     ├─ Graph.css
│     ├─ Graph.js
│     ├─ GroupChatBox.js
│     ├─ GroupChats.js
│     ├─ GroupMessage.css
│     ├─ GroupMessage.js
│     ├─ Home.css
│     ├─ Home.js
│     ├─ Message.css
│     ├─ Message.js
│     ├─ MyOffers.js
│     ├─ Nav.css
│     ├─ Nav.js
│     ├─ Predictor.css
│     ├─ Predictor.js
│     ├─ RatesSideBar.css
│     ├─ RatesSideBar.js
│     ├─ Transactions
│     │  ├─ Transactions.css
│     │  └─ Transactions.js
│     ├─ UserChatBox.css
│     ├─ UserChatBox.js
│     ├─ UserChats.js
│     ├─ UserContext.js
│     ├─ UserCredentialsDialog
│     │  ├─ UserCredentialsDialog.css
│     │  └─ UserCredentialsDialog.js
│     ├─ index.css
│     ├─ index.js
│     ├─ localstorage.js
│     ├─ logo.svg
│     ├─ reportWebVitals.js
│     └─ setupTests.js
└─ README.md

```