# Lumplet - Second Hand Store Mobile App

## Introduction
Welcome to Lumplet, a mobile application designed for a second-hand store. This app allows users to browse and purchase a variety of second-hand items ranging from clothing to accessories, with a user-friendly interface and secure authentication.

## Features
- **User Authentication**: Secure sign-in and sign-up functionalities using email/password and Google SignIn.
- **Item Browsing**: Users can browse items by categories such as sneakers, clothing, and accessories.
- **Dynamic Filtering**: Ability to filter items based on name and price.
- **Cart Management**: Users can add items to a cart, view cart contents, and manage them.
- **Item Details**: Detailed view of each item including images, descriptions, and pricing.
- **Image Loading**: Integration with Firebase Storage for image handling.

## Technical Aspects
- **Authentication Class**: Handles all authentication processes using Firebase Auth and Google SignIn.
- **Cart Singleton**: Manages the user's shopping cart.
- **Item Class**: Represents an item, implementing `Parcelable` for easy data passing.
- **ItemList Activity**: Manages the display and filtering of items.
- **ProductView Activity**: Detailed view for individual items, with add-to-cart functionality.

## Installation and Setup
To set up the Lumplet app, clone the repository and import it into Android Studio. Ensure you have the latest version of Android Studio and the Android SDK.

## Usage
1. **Sign In/Sign Up**: Start by signing in or creating a new account.
2. **Browse Items**: Navigate through different categories to find items.
3. **Filter and Select**: Apply filters as needed and select items to view more details.
4. **Add to Cart**: Add items to your cart for purchase.
5. **Checkout**: Proceed to checkout from the cart.

## Dependencies
- Firebase Authentication
- Google SignIn Services
- Firebase Firestore
- Firebase Storage
- Glide for image loading

## License
Lumplet is released under the [MIT License](LICENSE). See the LICENSE file for more details.
