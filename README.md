
# Terminal-Based Bad Apple ASCII Art Renderer in Kotlin

This Kotlin program extracts frames from the "bad_apple.mp4" video file and renders them as ASCII art in the terminal while playing the accompanying audio. It's inspired by the "Bad Apple!!" music video.

## Review 
![Review](/src/review.gif)

## Installation and Usage

1. **Clone the Repository**: Clone this repository to your local machine.

   ```bash
   git clone https://github.com/ShinniUwU/BadAppleKT.git
   ```

2. **Navigate to the Directory**: Change directory to the cloned repository.

   ```bash
   cd BadAppleKT
   ```

3. **Compile and Run**: Compile and run the Kotlin program using Gradle.

   ```bash
   ./gradlew run
   ```

## Usage

Upon running the program, it will extract frames from the "bad_apple.mp4" video file and render them as ASCII art in the terminal. The audio will simultaneously play in sync with the ASCII animation.

## Customization

- **Frame Rate**: Adjust the `frameRate` variable in the `main()` function to change the speed of the animation.
- **ASCII Characters**: Modify the `asciiChars` array in the `convertToAscii()` function to customize the ASCII representation.

## Credits

This program is inspired by the ["BadAppleK"](https://github.com/fumiya-kume/BadAppleK?tab=readme-ov-file) repo.

## License

This project is licensed under the [GNU General Public License (GPL)](LICENSE).

## Author

Developed by Hana (ME :3).

Feel free to contribute, report issues, or suggest improvements!

---

*So far, it's been tested only on Linux Kitty terminal. It needs to be made compatible with a full range of terminals. Please note that I won't be actively working on this repository. However, if someone wants to push updates, feel free to do so.*

