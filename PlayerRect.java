package mazeFiles;

import java.awt.Rectangle;

class PlayerRect extends Rectangle{
	
	int speed  = 1;
	
	PlayerRect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
}
