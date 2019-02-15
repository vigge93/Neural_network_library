//Initialize constants and variables
static final int BLANK = 0;
static final int APPLE = 1;
static final int SNAKEBODYUP = -1;
static final int SNAKEBODYRIGHT = -2;
static final int SNAKEBODYDOWN = -3;
static final int SNAKEBODYLEFT = -4;
static final int SNAKEHEAD = -5;
static final int DIRUP = 0;
static final int DIRRIGHT = 1;
static final int DIRDOWN = 2;
static final int DIRLEFT = 3;
static final int TEXTSIZE = 25;
final color SNAKECOLOR = color(0, 150, 100);
final color APPLECOLOR = color(255, 0, 0);
final color BACKGROUNDCOLOR = color(200, 200, 200);
final color HEADCOLOR = color(0, 120, 70);
final color TEXTCOLOR = color(0, 0, 0);

int[][] game_cells = new int[GAMESIZE][GAMESIZE];    //game_cells[y][x]
int direction, score;
int snake_head_x, snake_head_y, snake_tail_x, snake_tail_y;
boolean key_pressed;
int high_score = 0, game_number = 0;

void reset() {
  for (int y = 0; y<GAMESIZE; y++) {
    for (int x = 0; x<GAMESIZE; x++) {
      game_cells[y][x] = BLANK;
    }
  }
  direction = 0;
  score = 0;
  create_snake();
  create_apple();
}

void update() {
  if (snake_is_dead()) {
    high_score = max(score, high_score);
    reset();
  }
  if (apple_is_eaten()) {
    score++;
    move_and_add();
    create_apple();
  } else
    move_snake();
}

boolean snake_is_dead() {
  switch(direction)
  {
    //UP
  case DIRUP:
    if (snake_head_y == 0) {
      return true;
    }
    if (snake_head_y > 1 && game_cells[snake_head_y-2][snake_head_x] == SNAKEBODYDOWN) {
      return true;
    }
    if (snake_head_x < GAMESIZE-1 && game_cells[snake_head_y-1][snake_head_x+1] == SNAKEBODYLEFT) {
      return true;
    }
    if (snake_head_x > 0 && game_cells[snake_head_y-1][snake_head_x-1] == SNAKEBODYRIGHT) {
      return true;
    }
    break;

    //RIGHT
  case DIRRIGHT:
    if (snake_head_x == GAMESIZE-1) {
      return true;
    }
    if (snake_head_x < GAMESIZE-3 && game_cells[snake_head_y][snake_head_x+2] == SNAKEBODYLEFT) {
      return true;
    }
    if (snake_head_y < GAMESIZE-1 && game_cells[snake_head_y+1][snake_head_x+1] == SNAKEBODYUP) {
      return true;
    }
    if (snake_head_y > 0 && game_cells[snake_head_y-1][snake_head_x+1] == SNAKEBODYDOWN) {
      return true;
    }
    break;

    //DOWN
  case DIRDOWN:
    if (snake_head_y == GAMESIZE-1) {
      return true;
    }
    if (snake_head_y < GAMESIZE-3 && game_cells[snake_head_y+2][snake_head_x] == SNAKEBODYUP) {
      return true;
    }
    if (snake_head_x < GAMESIZE-1 && game_cells[snake_head_y+1][snake_head_x+1] == SNAKEBODYLEFT) {
      return true;
    }
    if (snake_head_x > 0 && game_cells[snake_head_y+1][snake_head_x-1] == SNAKEBODYRIGHT) {
      return true;
    }
    break;

    //LEFT
  case DIRLEFT:
    if (snake_head_x == 0) {
      return true;
    }
    if (snake_head_x > 1 && game_cells[snake_head_y][snake_head_x-2] == SNAKEBODYRIGHT) {
      return true;
    }
    if (snake_head_y < GAMESIZE-1 && game_cells[snake_head_y+1][snake_head_x-1] == SNAKEBODYUP) {
      return true;
    }
    if (snake_head_y > 0 && game_cells[snake_head_y-1][snake_head_x-1] == SNAKEBODYDOWN) {
      return true;
    }
    break;
  }
  return false;
}

//Move after eating an apple
void move_and_add() {
  switch(direction) {
  case DIRUP:
    game_cells[snake_head_y][snake_head_x] = SNAKEBODYUP;
    game_cells[snake_head_y-1][snake_head_x] = SNAKEHEAD;
    snake_head_y--;
    break;
  case DIRRIGHT:
    game_cells[snake_head_y][snake_head_x] = SNAKEBODYRIGHT;
    game_cells[snake_head_y][snake_head_x+1] = SNAKEHEAD;
    snake_head_x++;
    break;
  case DIRDOWN:
    game_cells[snake_head_y][snake_head_x] = SNAKEBODYDOWN;
    game_cells[snake_head_y+1][snake_head_x] = SNAKEHEAD;
    snake_head_y++;
    break;
  case DIRLEFT:
    game_cells[snake_head_y][snake_head_x] = SNAKEBODYLEFT;
    game_cells[snake_head_y][snake_head_x-1] = SNAKEHEAD;
    snake_head_x--;
    break;
  }
}

void create_snake() {
  game_cells[GAMESIZE/2][GAMESIZE/2] = SNAKEHEAD;
  game_cells[GAMESIZE/2+1][GAMESIZE/2] = SNAKEBODYUP;
  snake_head_x = GAMESIZE/2;
  snake_head_y = GAMESIZE/2;
  snake_tail_x = GAMESIZE/2;
  snake_tail_y = GAMESIZE/2+1;
}

void move_snake() {
  if (game_cells[snake_tail_y][snake_tail_x] == SNAKEBODYUP) {
    game_cells[snake_tail_y][snake_tail_x] = BLANK;
    snake_tail_y--;
  } else if (game_cells[snake_tail_y][snake_tail_x] == SNAKEBODYRIGHT) {
    game_cells[snake_tail_y][snake_tail_x] = BLANK;
    snake_tail_x++;
  } else if (game_cells[snake_tail_y][snake_tail_x] == SNAKEBODYDOWN) {
    game_cells[snake_tail_y][snake_tail_x] = BLANK;
    snake_tail_y++;
  } else if (game_cells[snake_tail_y][snake_tail_x] == SNAKEBODYLEFT) {
    game_cells[snake_tail_y][snake_tail_x] = BLANK;
    snake_tail_x--;
  }
  switch(direction) {
  case DIRUP:
    game_cells[snake_head_y][snake_head_x] = SNAKEBODYUP;
    game_cells[snake_head_y-1][snake_head_x] = SNAKEHEAD;
    snake_head_y--;
    break;
  case DIRRIGHT:
    game_cells[snake_head_y][snake_head_x] = SNAKEBODYRIGHT;
    game_cells[snake_head_y][snake_head_x+1] = SNAKEHEAD;
    snake_head_x++;
    break;
  case DIRDOWN:
    game_cells[snake_head_y][snake_head_x] = SNAKEBODYDOWN;
    game_cells[snake_head_y+1][snake_head_x] = SNAKEHEAD;
    snake_head_y++;
    break;
  case DIRLEFT:
    game_cells[snake_head_y][snake_head_x] = SNAKEBODYLEFT;
    game_cells[snake_head_y][snake_head_x-1] = SNAKEHEAD;
    snake_head_x--;
    break;
  }
}

boolean apple_is_eaten() {
  for (int y = 0; y<GAMESIZE; y++) {
    for (int x = 0; x<GAMESIZE; x++) {
      if (game_cells[y][x] == APPLE) {
        switch(direction) {
        case DIRDOWN:
          if (y>0)
            if (game_cells[y-1][x]==SNAKEHEAD)
              return true;
          break;
        case DIRRIGHT:
          if (x>0)
            if (game_cells[y][x-1] == SNAKEHEAD)
              return true;
          break;
        case DIRUP:
          if (y<GAMESIZE-1)
            if (game_cells[y+1][x]==SNAKEHEAD)
              return true;
          break;
        case DIRLEFT:
          if (x<GAMESIZE-1)
            if (game_cells[y][x+1] == SNAKEHEAD)
              return true;
          break;
        }
        return false;
      }
    }
  }
  return true;
}

void create_apple() {
  ArrayList<int[]> available = new ArrayList<int[]>();
  for (int i = 0; i < GAMESIZE; i++) {
    for (int j = 0; j < GAMESIZE; j++) {
      if (game_cells[i][j] == BLANK) {
        available.add(new int[]{i, j});
      }
    }
  }
  if (available.size() > 0) {
    int[] pos = available.get(int(random(available.size())));
    int rx = pos[1];
    int ry = pos[0];
    game_cells[ry][rx] = APPLE;
  }
}