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
│     │     │  │           ├─ ChatsActivity.kt
│     │     │  │           ├─ ChatsFragment.kt
│     │     │  │           ├─ Convo.kt
│     │     │  │           ├─ ExchangeFragment.kt
│     │     │  │           ├─ GroupConvo.kt
│     │     │  │           ├─ GroupMessageAdapter.kt
│     │     │  │           ├─ GroupWithLastMessage.kt
│     │     │  │           ├─ GroupsFragment.kt
│     │     │  │           ├─ GroupsRVAdapter.kt
│     │     │  │           ├─ LoginActivity.kt
│     │     │  │           ├─ MainActivity.kt
│     │     │  │           ├─ MessageAdapter.kt
│     │     │  │           ├─ Myoffers.kt
│     │     │  │           ├─ RegistrationActivity.kt
│     │     │  │           ├─ TabsPagerAdapter.kt
│     │     │  │           ├─ TradingFragment.kt
│     │     │  │           ├─ TransactionsFragment.kt
│     │     │  │           ├─ UserWithLastMessage.kt
│     │     │  │           ├─ UsersRVAdapter.kt
│     │     │  │           └─ api
│     │     │  │              ├─ Authentication.kt
│     │     │  │              ├─ model
│     │     │  │              │  ├─ ExchangeRates.kt
│     │     │  │              │  ├─ Group.kt
│     │     │  │              │  ├─ GroupMessage.kt
│     │     │  │              │  ├─ Message.kt
│     │     │  │              │  ├─ Offer.kt
│     │     │  │              │  ├─ Token.kt
│     │     │  │              │  ├─ Transaction.kt
│     │     │  │              │  └─ User.kt
│     │     │  │              └─ retrofit.kt
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
│     │     │     │  ├─ activity_calculator.xml
│     │     │     │  ├─ activity_chats.xml
│     │     │     │  ├─ activity_convo.xml
│     │     │     │  ├─ activity_group_convo.xml
│     │     │     │  ├─ activity_login.xml
│     │     │     │  ├─ activity_main.xml
│     │     │     │  ├─ activity_myoffers.xml
│     │     │     │  ├─ activity_registration.xml
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
│     │     │     ├─ values-land
│     │     │     │  └─ dimens.xml
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
│  ├─ __init__.py
│  ├─ app.py
│  ├─ config.py
│  ├─ files
│  │  ├─ Predict.py
│  │  ├─ data.csv
│  │  └─ testPrediction.py
│  ├─ models
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
│           │  │        │     ├─ GroupMessage.java
│           │  │        │     ├─ Message.java
│           │  │        │     ├─ Offer.java
│           │  │        │     ├─ Token.java
│           │  │        │     ├─ Transaction.java
│           │  │        │     └─ User.java
│           │  │        ├─ chat
│           │  │        │  └─ Chat.java
│           │  │        ├─ login
│           │  │        │  └─ Login.java
│           │  │        ├─ rates
│           │  │        │  └─ Rates.java
│           │  │        ├─ register
│           │  │        │  └─ Register.java
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
│                       ├─ login
│                       │  ├─ login.css
│                       │  └─ login.fxml
│                       ├─ parent.css
│                       ├─ parent.fxml
│                       ├─ rates
│                       │  ├─ rates.css
│                       │  └─ rates.fxml
│                       ├─ register
│                       │  ├─ register.css
│                       │  └─ register.fxml
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
│     ├─ ChatDrawer.css
│     ├─ ChatDrawer.js
│     ├─ ChatUser.js
│     ├─ CreateOffer.js
│     ├─ DrawerNav.js
│     ├─ Home.css
│     ├─ Home.js
│     ├─ Message.css
│     ├─ Message.js
│     ├─ Nav.css
│     ├─ Nav.js
│     ├─ Offers.js
│     ├─ RatesSideBar.css
│     ├─ RatesSideBar.js
│     ├─ UserChatBox.css
│     ├─ UserChatBox.js
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
├─ LSTM_Exchange_Rate_Predictor.pth
├─ README.md
└─ scaler.joblib

```