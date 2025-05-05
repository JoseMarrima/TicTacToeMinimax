## ✅ Tic Tac Toe with AI – Git Commit Tickets

---

### 🎫 Ticket 1 – Setup Game Board UI with Swing  
**Commit Message:**  
`feat: create Swing UI layout and game board`

**Includes:**
- `JFrame` window with 3×3 `GridLayout`
- 9 buttons displayed on the board
- Basic button styling
- Button click handling (no game logic yet)
- Board state initialized with `' '` characters

---

### 🎫 Ticket 2 – Implement Player Move and Game Over Detection  
**Commit Message:**  
`feat: add player move handling and win/tie check`

**Includes:**
- Update board state and button text on player click
- Win/tie detection via `checkWinner()`
- Show message dialog when the game ends
- Reset the board after game over

---

### 🎫 Ticket 3 – Add Minimax Algorithm for AI Move  
**Commit Message:**  
`feat: implement AI opponent using Minimax algorithm`

**Includes:**
- AI makes move after the player
- Minimax logic to choose best move
- Game-over detection after AI move
- Turn switching between player and AI

---

### 🎫 Ticket 4 – Final Polish and Game Reset Logic  
**Commit Message:**  
`chore: improve reset logic and turn management`

**Includes:**
- Improved reset (clears board and buttons)
- Turn alternates properly (player starts)
- Ignore clicks on already filled cells
- Clean-up and inline comments for clarity
