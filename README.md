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
- Logout from app
- Reset password
- Verify Email
- Get all users
- Update user role by email
- Update user role by id
- Find user by id
- Delete user by id
- Find user by email
- Update user image by email
- Update user name by email
- Update user last-name by email
- Delete user by email
- Update user password by email
- Update user birthday by email

## TrainingProgram 🏃‍♂️
- Create a new training program
- Get all training pograms
- Get training program by id
- Soft-delete training program by id
- Update training program by ID
- Get all training programs by date
- Get program by date
- Update training program name by id
- Update training program about by id
- Update training program date by id
- Update training program image by id

## TrainingSection
- Create a new training section
- Get all training sections
- Get training section by id
- Soft-delete training section by id
- Update training section by id
- Update training section name by id
- Update training section image by id 
- Update training section program by id

## Exercise
- Create a new exercise
- Get all exercises
- Get exercise by id
- Soft-delete exercise by id
- Update exercise by id
- Update exercise name by id
- Update exercise about by id
- Update exercise image by id
- Update exercise section by id

## Video
- Create a new video
- Get all videos
- Get video by id
- Soft-delete video by id

## PasswordReset
- Reset password by email

## Support
- Send email to support
  
## Role 🙎‍♂️
- Create/update/remove a role
- Get role by roleName

# Controllers 🕹

## Auth
- Post | register user - /register
- Post | login user - /login
- Post | logout from app  - /logout
- Put | reset password - /reset/{email}
- Get | verify email - /verify/{email}

## User
- Get | display all users - /users
- Delete | soft delete user by id - /users/{id}
- Get | find user information by id - /users/me/{id}
- Put | update user information by id - /users/me/{id}
- Put | update user role by id - /users/role/{id}
- Put | update user role by email - /users/role/{email}
- Put | update user by email - /users/me/{email}
- Put | update user image by email - /users/me/image/{email}
- Put | update user name by email - /users/me/name/{email}
- Put | update user last-name by email - /users/me/last-name/{email}
- Delete | delete user by email - /users/me/{email}
- Put | Update user password by email - /users/me/password/{email}
- Put | Update user birthday by email - /users/me/birthday/{email}

## TrainingProgram 
- Post | Create a new training program - /programs
- Get | Get all training pograms - /programs
- Get | Get training program by id /programs/{id}
- Delete | Soft-delete training program by id - /programs/{id}
- Put | Update training program by ID - /programs/{id}
- Get | Get all training programs by date - /programs/by-date/{date}
- Get | Get program by date - /programs/program-by-date/{date}
- Put | Update training program name by id - /programs/name/{id}
- Put | Update training program about by id - /programs/about/{id}
- Put | Update training program date by id - /programs/date/{id}
- Put | Update training program image by id - /programs/image/{id}

## TrainingSection
- Post | Create a new training section - /sections
- Get | Get all training sections - /sections
- Get | Get training section by id - /sections/{id}
- Delete | Soft-delete training section by id - /sections/{id}
- Put | Update training section by id - /sections/{id}
- Put | Update training section name by id /sections/name/{id}
- Put | Update training section image by id /sections/image/{id}
- Put | Update training section program by id /sections/program/{id}

## Exercise
- Post | Create a new exercise - /exercises
- Get | Get all exercises - /exercises
- Get | Get exercise by id - /exercises/{id}
- Delete | Soft-delete exercise by id - /exercises/{id}
- Put | Update exercise by id - /exercises/{id}
- Put | Update exercise name by id - /exercises/name/{id}
- Put | Update exercise about by id - /exercises/about/{id}
- Put | Update exercise image by id - /exercises/image/{id}
- Put | Update exercise section by id - /exercises/section/{id}

## Video
- Post | Create a new video - /videos
- Get | Get all videos - /videos
- Get | Get video by id - /videos{id}
- Delete | Soft-delete video by id - /videos/{id}

## PasswordReset
- Put | Reset password by email - /password/reset/{email}

## Support
- Post | Send email to support - /support/{email}

## Another Pages And Controllers 📄
- PaymentIntentController
- HomeController
- PartnersController
- PricingController
- SubscriptionController
- checkout.html
- home.html
- partners.html
- pricing.html
