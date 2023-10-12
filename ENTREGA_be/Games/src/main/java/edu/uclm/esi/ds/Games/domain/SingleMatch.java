package edu.uclm.esi.ds.Games.domain;

public class SingleMatch extends Match {

	protected String player;
	protected Board board;
	
	public SingleMatch() {
		this.player = "";
		this.board = new Board();
		this.setReady(true);
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}
	
	public void addPlayer(String player) {
		this.setPlayer(player);
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Match matchNumbers(int x1, int y1, int x2, int y2) throws Exception{
		this.board.matchNumbers(x1,y1,x2,y2);
		return this;
	}
	
	public Match addRows() {
		this.board.addRows();
		return this;
	}
}