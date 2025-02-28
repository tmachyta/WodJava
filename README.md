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

## User  🤵‍♂️
- Registration like a user
- Authentication like a user
- Create/update/remove a user
- Display all users
- Update user role by email
- Update user role by id
- Find user by id

## Car 🏎
- Create/update/remove a car
- Find car by id
- Display all available cars

## Payment 💵
- Create/update/remove a payment
- Display all payments
- Find payment by id
- Find payment by rental id
- Find payment by user id
- Create payment session
- Check successful payment
- Handle canceled payment

## Rental 💵
- Display all rentals
- Find rental by id
- Find rental by car id
- Find rental by user id
- Return rental
- Create/update/remove a rental

## Role 🙎‍♂️
- Create/update/remove a role
- Get role by roleName

# Controllers 🕹

## Auth
- Post - /register
- Post - /login

## Car
- Get | display all cars - /cars
- Post | add car to repository - /cars
- Get | find car by id - /cars/{id}
- Delete | soft delete car by id - /cars{id}
- Put | update - /cars{id}

## User
- Get | display all users - /users
- Delete | soft delete user by id - /users/{id}
- Get | find user information by id - /users/me/{id}
- Put | update user information by id - /users/me/{id}
- Put | update user role by id - /users/role/{id}
- Put | update user role by email - /users/role/{email}

## Rental
- Get | display all rentals - /rentals
- Post | save rental to repositort - /rentals
- Get | find rental by id - /rentals/{id}
- Get | find rental by car id - /rentals/car/{id}
- Get | find rental by user id - /rentals/user/{id}
- Put | update rental by id - /rentals/{id}
- Delete | soft delete by id - /rentals/{id}

## Payment
- Get | display all payments - /payments
- Post | save payment to repository - /payments
- Get | find payment by id - /payments/{id}
- Get | find payment by rental id - /payments/rental/{id}
- Get | find payment by user id - /payments/user/{id}
- Post | create success payment session - /payments/create-session
- Get | get success payment session - /payments/success
- Get | get cancel payment by id - /payments/cancel/{id}
