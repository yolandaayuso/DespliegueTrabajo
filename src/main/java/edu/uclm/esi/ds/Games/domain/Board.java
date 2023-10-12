package edu.uclm.esi.ds.Games.domain;

import java.util.ArrayList;

public class Board {

	private int [][] digits = new int[9][9];
	private int numRows = 3;
	private boolean end = false;
	private boolean lost = false;
	
	public Board() {
		
		/*int [][] digitos = {{0,0,0,0,0,0,9,0,0},
							{0,0,0,0,2,3,0,0,4},
							{0,8,0,0,0,0,0,0,0},
							{0,0,7,0,0,0,0,0,2},
							{9,6,0,0,0,0,0,0,0},
							{7,5,9,0,0,0,0,0,0},
							{0,0,0,5,0,9,0,0,0},
							{0,0,0,0,0,4,8,0,0},
							{0,1,0,4,0,1,1,7,9}};
		
		for(int i = 0; i < this.digits.length; i++) {
			for(int j = 0; j < this.digits.length; j++) {
				this.digits[i][j] = digitos[i][j];
			}
		}*/
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {this.digits[i][j] = (byte)((Math.random()*9)+1);}}
		
	}

	public Board copy() {
		Board result = new Board();
		for(int i = 0; i < 3; i++) {for(int j = 0; j < 9; j++) {result.digits[i][j] = this.digits[i][j];}}
		return result;
	}

	public ArrayList<Integer> getDigits() {
		ArrayList<Integer> lista = new ArrayList<>();
		for (int[] fila : digits) {
		    for (int elemento : fila) {
		        lista.add(elemento);
		    }
		}
		return lista;
	}

	public Board matchNumbers(int x1, int y1, int x2, int y2) throws Exception {
		if(right_down(x1,y1,x2,y2) || left_up(x1,y1,x2,y2) || right_up(x1,y1,x2,y2) || left_down(x1,y1,x2,y2) 
				|| up(x1,y1,x2,y2) || down(x1,y1,x2,y2) || right(x1,y1,x2,y2) || left(x1,y1,x2,y2)) {
			this.digits[x1][y1] = 0;
			this.digits[x2][y2] = 0;
			this.checkEmptyRows();
			this.checkEmptyRows();
			if(this.numRows == 0) {this.end = true;}
			if(!checkRemainingMovements() && this.numRows == 9) {
				this.lost = true;
			}
		}else {
			throw new Exception("Forbbiden movement");
		}
		return this;
	}
	
	private boolean checkRemainingMovements() {
		boolean result = false;
		for(int i = 0; i < this.numRows && !result; i++) {
			for(int j = 0; j < this.digits.length && !result; j++) {
				if(checkMovementsRight(i,j) || checkMovementsLeft(i,j) || checkMovementsUp(i,j) || checkMovementsDown(i,j) || checkMovementsRightUp(i,j) 
						|| checkMovementsRightDown(i,j) || checkMovementsLeftUp(i,j) || checkMovementsLeftDown(i,j)) {
					result = true;
				}
			}
		}
		return result;
	}

	private boolean checkMovementsLeftDown(int i, int j) {
		boolean result = false;
		int x1c = i, y1c = j;
		try {
			while(y1c < this.numRows) {
				if(this.digits[++x1c][--y1c] == 0) {
					continue;
				}else {
					result = this.checkNumbers(i, j, x1c, y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}

	private boolean checkMovementsLeftUp(int i, int j) {
		boolean result = false;
		int x1c = i, y1c = j;
		try {
			while(x1c != 0) {
				if(this.digits[--x1c][--y1c] == 0) {
					continue;
				}else {
					result = this.checkNumbers(i, j, x1c, y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}

	private boolean checkMovementsRightDown(int i, int j) {
		boolean result = false;
		int x1c = i, y1c = j;
		try {
			while(y1c < this.numRows-1) {
				if(this.digits[++x1c][++y1c] == 0) {
					continue;
				}else {
					result = this.checkNumbers(i, j, x1c, y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}

	private boolean checkMovementsRightUp(int i, int j) {
		boolean result = false;
		int x1c = i, y1c = j;
		try {
			while(x1c != 0) {
				if(this.digits[--x1c][++y1c] == 0) {
					continue;
				}else {
					result = this.checkNumbers(i, j, x1c, y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}

	private boolean checkMovementsDown(int i, int j) {
		boolean result = false;
		int x1c = i, y1c = j;
		try {
			while(x1c < this.numRows-1) {
				if(this.digits[++x1c][y1c] == 0) {
					continue;
				}else {
					result = this.checkNumbers(i, j, x1c, y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}

	private boolean checkMovementsUp(int i, int j) {
		boolean result = false;
		int x1c = i, y1c = j;
		try {
			while(x1c != 0) {
				if(this.digits[--x1c][y1c] == 0) {
					continue;
				}else {
					result = this.checkNumbers(i, j, x1c, y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}

	private boolean checkMovementsLeft(int i, int j) {
		boolean result = false, located = false;
		int x1c = i, y1c = j;
		y1c--;
		try {
			for(;x1c >= 0 && !located; x1c--) {
				for(; y1c >= 0 && !located; y1c--) {
					if(this.digits[x1c][y1c] == 0) {
						continue;
					}else {
						result = this.checkNumbers(i, j, x1c, y1c);
						located = true;
					}
				}
				j = this.digits.length-1;
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}

	private boolean checkMovementsRight(int i, int j) {
		boolean result = false, located = false;
		int x1c = i, y1c = j;
		y1c++;
		try {
			for(; x1c < this.digits.length-1 && !located; x1c++) {
				for(; y1c <= this.digits.length-1 && !located; y1c++) {
					if(this.digits[x1c][y1c] == 0) {
						continue;
					}else {
						result = this.checkNumbers(i, j, x1c, y1c);
						located = true;
					}
				}
				y1c = 0;
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}

	public Board addRows() {
		int row = this.numRows, column = this.lastNumber(), index = 0;
		if(column != 0) {row--;}
		ArrayList<Integer> list = obtainAllNumbers();
		try {
			while(index < list.size()) {
				this.digits[row][column] = list.get(index);
				index++; column++;
				if(column == 9) {column = 0; row++;}
			}
			this.numRows = countRows();
		}catch(ArrayIndexOutOfBoundsException e) {this.numRows = countRows();}
		if(!checkRemainingMovements() && this.numRows == 9 ) {this.lost = true;}
		return this;
	}
	
	private ArrayList<Integer> obtainAllNumbers() {
		ArrayList<Integer> list = new ArrayList<>();
		for(int i = 0; i < this.digits.length; i++) {
			for(int j = 0; j < this.digits.length; j++) {
				if(this.digits[i][j] != 0) {
					list.add(this.digits[i][j]);
				}
			}
		}
		return list;
	}
	
	private int countRows() {
		int row = 0;
		for(int i = 0; i < this.digits.length; i++) {
		    boolean tieneNumeros = false;
		    for(int j = 0; j < this.digits[i].length; j++) {
		        if(this.digits[i][j] != 0) {
		            tieneNumeros = true;
		            break;
		        }
		    }
		    if(tieneNumeros) {
		    	row++;
		    }
		}
		return row;
	}
	
	private int lastNumber() {
		int row = this.numRows;
		int lastPosition = 0;
		row--;
		for(int j = 8; j >= 0; j--) {
			if(this.digits[row][j] == 0) {
				continue;
			}else {
				lastPosition = j+1;
				break;
			}
		}
		if(lastPosition == 9) {lastPosition = 0;}
		return lastPosition;
	}
	
	private void checkEmptyRows() {
		int counter = 0;
		for(int i = 0; i < this.numRows; i++) {
			for(int j = 0; j < 9; j++) {
				if(this.digits[i][j] == 0) {counter++;}
			}
			if(counter == 9) {
				this.restructureDigits(i);
				this.numRows--;
			}
			counter = 0;
		}
	}
	
	private void restructureDigits(int row) {
		int [][] copy = new int[9][9];
		int r = 0;
		for(int i = 0; i < this.digits.length; i++) {
			if(i == row) {continue;}
			for(int j = 0; j < this.digits.length; j++) {
				copy[r][j] = this.digits[i][j];
			}
			r++;
		}
		for(int i = 0; i < copy.length; i++) {
			for(int j = 0; j < copy.length; j++) {
				this.digits[i][j] = copy[i][j];
			}
		}
	}
	
	private boolean checkNumbers(int x1, int y1, int x2, int y2) {
		boolean result = false;
		if((this.digits[x1][y1] == this.digits[x2][y2]) || (this.digits[x1][y1] + this.digits[x2][y2] == 10)) {
			result = true;
		}
		return result;
	}
	
	private boolean right_down(int x1, int y1, int x2, int y2) {
		boolean result = false;
		int x1c = x1, y1c = y1;
		try {
			while(y1c <= 8) {
				if(this.digits[++x1c][++y1c] == 0) {
					continue;
				}else {
					result = checkSamePosition(x1,y1,x2,y2,x1c,y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}
	
	private boolean left_up(int x1, int y1, int x2, int y2) {
		boolean result = false;
		int x1c = x1, y1c = y1;
		try {
			while(x1c != 0) {
				if(this.digits[--x1c][--y1c] == 0) {
					continue;
				}else {
					result = checkSamePosition(x1,y1,x2,y2,x1c,y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}
	
	private boolean right_up(int x1, int y1, int x2, int y2) {
		boolean result = false;
		int x1c = x1, y1c = y1;
		try {
			while(x1c != 0) {
				if(this.digits[--x1c][++y1c] == 0) {
					continue;
				}else {
					result = checkSamePosition(x1,y1,x2,y2,x1c,y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}
	
	private boolean left_down(int x1, int y1, int x2, int y2) {
		boolean result = false;
		int x1c = x1, y1c = y1;
		try {
			while(y1c <= 8) {
				if(this.digits[++x1c][--y1c] == 0) {
					continue;
				}else {
					result = checkSamePosition(x1,y1,x2,y2,x1c,y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}
	
	private boolean up(int x1, int y1, int x2, int y2) {
		boolean result = false;
		int x1c = x1, y1c = y1;
		try {
			while(x1c != 0) {
				if(this.digits[--x1c][y1c] == 0) {
					continue;
				}else {
					result = checkSamePosition(x1,y1,x2,y2,x1c,y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}
	
	private boolean down(int x1, int y1, int x2, int y2) {
		boolean result = false;
		int x1c = x1, y1c = y1;
		try {
			/* CAMBIO AQUI */
			while(x1c <= 8) {
				if(this.digits[++x1c][y1c] == 0) {
					continue;
				}else {
					result = checkSamePosition(x1,y1,x2,y2,x1c,y1c);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}
	
	private boolean right(int x1, int y1, int x2, int y2) {
		boolean result = false, located = false;
		int x1c = x1, y1c = y1;
		y1c++;
		try {
			/* CAMBIO AQUI */
			for(; x1c < this.digits.length && !located; x1c++) {
				for(; y1c <= this.digits.length-1 && !located; y1c++) {
					if(this.digits[x1c][y1c] == 0) {
						continue;
					}else {
						result = checkSamePosition(x1,y1,x2,y2,x1c,y1c);
						located = true;
					}
				}
				y1c = 0;
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}
	
	private boolean left(int x1, int y1, int x2, int y2) {
		boolean result = false, located = false;
		int x1c = x1, y1c = y1;
		y1c--;
		try {
			for(;x1c >= 0 && !located; x1c--) {
				for(;y1c >= 0 && !located; y1c--) {
					if(this.digits[x1c][y1c] == 0) {
						continue;
					}else {
						result = checkSamePosition(x1,y1,x2,y2,x1c,y1c);
						located = true;
					}
				}
				y1c = 8;
			}
		}catch(ArrayIndexOutOfBoundsException e) {}
		return result;
	}
	
	private boolean checkSamePosition(int x1, int y1, int x2, int y2, int x1c, int y1c) {
		boolean result = false;
		if(x1c == x2 && y1c == y2) {result = this.checkNumbers(x1, y1, x2, y2);}
		return result;
	}
	
	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}
	
	public boolean isLost() {
		return lost;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}

	@Override
	public String toString() {
		String cadena = "";
		for(int i = 0; i < this.digits.length; i++) {
			for(int j = 0; j < this.digits.length; j++) {cadena += this.digits[i][j] +" ";}
			cadena += "\n";
		}
		return cadena;
	}
}