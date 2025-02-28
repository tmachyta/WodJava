# WODWarrior
# ✈🏃🏻‍♂️🏃🏻‍♂️WODWarrior✈🏃🏻‍♂️🏃🏻‍♂️

# Project Description:
WODWarrior is a CrossFit training application designed to help users manage and track their workouts effectively. The app provides a seamless user experience with a SwiftUI frontend and a robust Java, Hibernate, and Spring backend.

For user support, WODWarrior features an integrated Telegram bot, allowing quick assistance and interaction. Additionally, Stripe API is implemented to handle subscriptions and payments securely.

## Setup

To set up the project, follow these steps:

### Prerequisites

Make sure you have the following software installed on your system:

- Java Development Kit (JDK) 17 or higher
- Apache Maven
- Apache Tomcat vesion 9 or higher
- DataBase: MYSQL
- docker

### Installation
- First of all, you should made your fork
- Second, clink on Code<> and clone link, after that open your Intellij Idea, click on Get from VCS
- past link, which you clone later

### Replace Placeholders:
To connect to your DB, Telegram and Stripe, you should replace PlaceHolders in application.properties
- Open package resources and open file env and application.properties in your project.
- Locate the placeholders that need to be replaced.
- These placeholders might include values such as
- spring.datasource.username=$MYSQL_USER -> replace with your MySQL
- spring.datasource.password=$MYSQL_PASSWORD -> replace with your password MySQL
- stripe.secretKey=your_stripe_secret_key -> replace your stripe secret key
- telegram.botToken=your_bot_token -> replace with your telegram bot token
- telegram.chatId=your_chat_id -> replace with your telegram chat id

# Features 🤌:

## 🏢 WODWarriors  🏢
WODWarriors provides a different functions. 
- Create a new training program
- Create a new training seciton
- Create exercise
- Display all programs
- Display all sections
- Display all exercises
- Delete programs
- Update programs
- Delete sections
- Delete exercises
- Add video to exercise

# Controllers 🕹
Let's talk about program:

- Upon opening the training section, you'll be presented with a stack of cards, each representing a different type of program (Gymnastics, Olympic Lifts, Strong - including Squats, Bench Press, Deadlift - Cardio, WOD, AMRAP).

- Swiping a card to the right navigates you to the training section where you can view various subsections like Warm Up, WOD, etc. Swiping a card to the left temporarily removes it from the stack for 10 seconds before it returns.

- In the training section, you can select a subsection to explore a variety of exercises. Tapping on an exercise will take you to a detailed page that includes a description and a video demonstrating how to perform the exercise.

- Additionally, I developed a VideoPicker feature that allows you to save videos using Swift Data.
