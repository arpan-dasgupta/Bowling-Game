import java.util.Arrays;
import java.util.HashMap;

public class Scorer {

    Lane lane;

    public Scorer(Lane lane) {
        this.lane = lane;
    }

    int getScore(Bowler Cur, int frame) {

        int[] curScore;
        int strikeballs = 0;
        int totalScore = 0;
        curScore = (int[]) lane.scores.get(Cur);
        for (int i = 0; i != 10; i++) {
            // System.out.println(lane.bowlIndex + " ;() ");
            lane.cumulScores[lane.bowlIndex][i] = 0;
        }
        int current = 2 * (frame - 1) + lane.ball - 1;
        // Iterate through each ball until the current one.
        for (int i = 0; i != current + 2; i++) {
            // Spare:
            int i1 = i / 2;
            if (i % 2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1 && i < 19) {
                // This ball was a the second of a spare.
                // Also, we're not on the current ball.
                // Add the next ball to the ith one in cumul.
                lane.cumulScores[lane.bowlIndex][i1] += curScore[i + 1] + curScore[i];
                if (i > 1) {
                    // lane.cumulScores[lane.bowlIndex][i/2] += lane.cumulScores[lane.bowlIndex][i/2
                    // -1];
                }
            } else if (i < current && i % 2 == 0 && curScore[i] == 10 && i < 18) {
                strikeballs = 0;
                // This ball is the first ball, and was a strike.
                // If we can get 2 balls after it, good add them to cumul.
                if (curScore[i + 2] != -1) {
                    strikeballs = 1;
                    if (curScore[i + 3] != -1) {
                        // Still got em.
                        strikeballs = 2;
                    } else if (curScore[i + 4] != -1) {
                        // Ok, got it.
                        strikeballs = 2;
                    }
                }
                if (strikeballs == 2) {
                    // Add up the strike.
                    // Add the next two balls to the current cumulscore.
                    lane.cumulScores[lane.bowlIndex][i1] += 10;
                    if (curScore[i + 1] != -1) {
                        lane.cumulScores[lane.bowlIndex][i1] += curScore[i + 1]
                                + lane.cumulScores[lane.bowlIndex][i1 - 1];
                        if (curScore[i + 2] != -1) {
                            if (curScore[i + 2] != -2) {
                                lane.cumulScores[lane.bowlIndex][i1] += curScore[i + 2];
                            }
                        } else {
                            if (curScore[i + 3] != -2) {
                                lane.cumulScores[lane.bowlIndex][i1] += curScore[i + 3];
                            }
                        }
                    } else {
                        if (i1 > 0) {
                            lane.cumulScores[lane.bowlIndex][i1] += curScore[i + 2]
                                    + lane.cumulScores[lane.bowlIndex][i1 - 1];
                        } else {
                            lane.cumulScores[lane.bowlIndex][i1] += curScore[i + 2];
                        }
                        if (curScore[i + 3] != -1) {
                            if (curScore[i + 3] != -2) {
                                lane.cumulScores[lane.bowlIndex][i1] += curScore[i + 3];
                            }
                        } else {
                            lane.cumulScores[lane.bowlIndex][i1] += curScore[i + 4];
                        }
                    }
                } else {
                    break;
                }
            } else {
                // We're dealing with a normal throw, add it and be on our way.
                if (i % 2 == 0 && i < 18) {
                    if (i1 == 0) {
                        // First frame, first ball. Set his cumul score to the first ball
                        if (curScore[i] != -2) {
                            lane.cumulScores[lane.bowlIndex][i1] += curScore[i];
                        }
                    } else if (i1 != 9) {
                        // add his last frame's cumul to this ball, make it this frame's cumul.
                        if (curScore[i] != -2) {
                            lane.cumulScores[lane.bowlIndex][i1] += lane.cumulScores[lane.bowlIndex][i1 - 1]
                                    + curScore[i];
                        } else {
                            lane.cumulScores[lane.bowlIndex][i1] += lane.cumulScores[lane.bowlIndex][i1 - 1];
                        }
                    }
                } else if (i < 18) {
                    if (curScore[i] != -1 && i > 2) {
                        if (curScore[i] != -2) {
                            lane.cumulScores[lane.bowlIndex][i1] += curScore[i];
                        }
                    }
                }
                if (i1 == 9) {
                    if (i == 18) {
                        lane.cumulScores[lane.bowlIndex][9] += lane.cumulScores[lane.bowlIndex][8];
                    }
                    if (curScore[i] != -2) {
                        lane.cumulScores[lane.bowlIndex][9] += curScore[i];
                    }
                } else if (i1 == 10) {
                    if (curScore[i] != -2) {
                        lane.cumulScores[lane.bowlIndex][9] += curScore[i];
                    }
                }
            }
        }
        return totalScore;
    }

    public void subscribe(LaneObserver adding) {
        lane.subscribers.add(adding);
    }

    public void unsubscribe(LaneObserver removing) {
        lane.subscribers.remove(removing);
    }

    public void publish(LaneEvent event) {
        if (lane.subscribers.size() > 0) {

            for (LaneObserver subscriber : lane.subscribers) {
                subscriber.receiveLaneEvent(event);
            }
        }
    }

    void markScore(Bowler Cur, int frame, int ball, int score) {
        int[] curScore;
        int index = ((frame - 1) * 2 + ball);

        curScore = (int[]) lane.scores.get(Cur);
        curScore[index - 1] = score;
        lane.scores.put(Cur, curScore);
        System.out.println(Arrays.deepToString(lane.cumulScores));
        getScore(Cur, frame);
        publish(lane.lanePublish());
    }
}
