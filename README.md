# Math Catcher

A Java-based math learning game that helps students practice and improve their mathematical skills through interactive gameplay.

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
- Java Development Kit (JDK) 8 or higher
- An IDE (IntelliJ IDEA, Eclipse, or VS Code)
- Git

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/mathCatcher.git
cd mathCatcher
```

### Open in Your IDE

#### IntelliJ IDEA
1. Open IntelliJ IDEA
2. File → Open
3. Select the `mathCatcher` folder
4. Wait for the IDE to index the project

#### Eclipse
1. Open Eclipse
2. File → Open Projects from File System
3. Select the `mathCatcher` folder
4. Click Finish

#### VS Code
1. Open VS Code
2. File → Open Folder
3. Select the `mathCatcher` folder
4. Install the Java Extension Pack if prompted

### Run the Project

1. Navigate to the main class in `src/` folder
2. Right-click on the main class
3. Select "Run" or "Run As → Java Application"

## 🤝 Contributing

### Workflow for Team Members

1. **Pull the latest changes before starting work:**
   ```bash
   git pull
   ```

2. **Create a new branch for your feature:**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes and commit regularly:**
   ```bash
   git add .
   git commit -m "Description of your changes"
   ```

4. **Push your branch to GitHub:**
   ```bash
   git push -u origin feature/your-feature-name
   ```

5. **Create a Pull Request on GitHub:**
   - Go to the repository on GitHub
   - Click "Pull requests" → "New pull request"
   - Select your branch and create the PR
   - Wait for team review and approval

### Best Practices

- **Always pull before starting work** to avoid conflicts
- **Write clear commit messages** describing what you changed
- **Test your code** before committing
- **Keep commits small and focused** on one feature/fix
- **Don't commit IDE-specific files** (already handled by .gitignore)
- **Communicate with your team** about what you're working on

## 📁 Project Structure

```
mathCatcher/
├── src/                  # Source code files (.java)
│   └── com/mathcatcher/  # Package structure
├── .gitignore           # Git ignore rules
└── README.md            # This file
```

## 🔧 Troubleshooting

### "Project won't compile"
- Make sure you have the correct JDK version installed
- Try cleaning and rebuilding the project in your IDE

### "Git conflicts"
- Run `git pull` to get the latest changes
- Resolve conflicts in your IDE
- Commit the resolved files

### "Missing dependencies"
- Check if your IDE has indexed the project correctly
- Try invalidating caches and restarting (IntelliJ: File → Invalidate Caches)

