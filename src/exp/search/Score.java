package exp.search;

import java.util.ArrayList;
import java.util.List;
/**
 * Scores provided by database search engine
 * 
 */

public class Score {
	private List<Double> scores;
	private int targetScoreIndex;
	private boolean smallerIsBetter;
	
	public Score() {
		this.scores = new ArrayList<Double>();
		this.targetScoreIndex = 0;
		this.smallerIsBetter = true;
	}
	/**
	 * Get score used for FDR calculation 
	 * @return score name 
	 */
	public double getFDRScore() {
		return this.scores.get(this.targetScoreIndex);
	}
	/**
	 * Set score used for FDR calculation
	 * @param scoreIndex index of scores among score list 
	 */
	public void setTargetScore(int scoreIndex) {
		this.targetScoreIndex = scoreIndex;
	}
	/**
	 * Get list of scores
	 * return list of scores
	 */
	public List<Double> getScores() {
		return this.scores;
	}
	/**
	 * Set list of scores provided by database tool
	 * @param scores
	 */
	public void setScores(List<Double> scores) {
		this.scores = scores;
	}
	/**
	 * Get score strength for FDR calculation
	 * @return
	 */
	public boolean getFDROrder() {
		return this.smallerIsBetter;
	}
	/**
	 * Set strength order of score 
	 * @param smallerIsBetter
	 */
	public void setFDROrder(boolean smallerIsBetter) {
		this.smallerIsBetter = smallerIsBetter;
	}
	/**
	 * Add score 
	 * @param score score 
	 */
	public void addScore(double score) {
		this.scores.add(score);
	}

}
