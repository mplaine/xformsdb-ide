//package fi.tkk.media.xide.client.utils;
//
///*
// * Copyright (c) 2008, Jacob J. Topper
// * All rights reserved.
// * Redistribution and use in source and binary forms, with or without modification,
// * are permitted provided that the following conditions are met:
// * 
// *  - Redistributions of source code must retain the above copyright notice, this 
// *    list of conditions and the following disclaimer.
// *    
// *  - Redistributions in binary form must reproduce the above copyright notice,
// *    this list of conditions and the following disclaimer in the documentation 
// *    and/or other materials provided with the distribution.
// *    
// *  - The name of the copyright holder may not be used to endorse or promote 
// *    products derived from this software without specific prior written permission.
// *    
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS 
// * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
// * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN 
// * NO EVENT SHALL THE COPYRIGHT OWNER BE LIABLE FOR ANY DIRECT, INDIRECT,
// * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
// * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
// * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
// * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
// * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//
//import com.MJC.client.Animations.Animation;
//import com.MJC.client.Core.Posn;
//
//import com.google.gwt.user.client.ui.RootPanel;
//import com.google.gwt.user.client.ui.Widget;
//
//import fi.tkk.media.xide.client.Main;
//
///**
// * Emulate bounce effect of an object. Bounce effect is a series of iterations.
// * Each iteration is a movement when the object goes up and falls down as if it
// * would be thrown up.
// * 
// * @author Jacob Topper
// */
//public class BounceAnimationEffect extends Animation { // toggle should be set
//	// up here
//	// Acceleration of the bouncing object
//	public static final double ACCSELERATION = 0.1;
//	// Defines how many times and on what height(in pixels) the object will
//	// bounce
//	public static final int[] POSS = { 20, 10, 5 };
//
//	// Main time variable which counts how many frames has been
//	// processes since current iteration of bounce has started
//	private int time;
//	// Indicates whether the object is now falling down or going up
//	private boolean isFallingDown;
//	// Time limit for the current iteration. Shows how many frames should each
//	// face of iteration last.
//	private double t0;
//	// Number of iteration which processed now
//	private int count = 0;
//
//	/** The ending position */
//	private Posn end;
//	/** The distance between the current position and end. starts at 0 */
//	private double current;
//	/** The distance between start and end */
//	private double target;
//	/** The pre-calculated value for cos(angle) */
//	private double dx;
//	/** the pre-calculated value for sin(angle) */
//	private double dy;
//
//	/**
//	 * @param <b>w</b> the widget which will be moved by this animation.
//	 * @param <b>begin</b> the starting position for <i>widget</i>, assuming
//	 *        that it hasn't already been set, or that it isn't reset later.
//	 *        <p>
//	 *        <b>NOTE:</b> The actual stored variable is the Panel that contains
//	 *        the given widget. If the widget isn't already in a Panel, a
//	 *        SimplePanel will be automatically generated and the widget placed
//	 *        in it. If the panel that contains this widget is the RootPanel,
//	 *        this still fails, so please don't.
//	 *        </p>
//	 */
//	public BounceAnimationEffect(Widget w, Posn begin) {
//		super(w, begin, 3, 40);
//		reset();
//	}
//
//	/**
//	 * This method is called when new iteration of bouncing is planned. It
//	 * calculates the time and initial speed according to given acceleration and
//	 * height
//	 */
//	private void calculateTimeAndSpeed() {
//		// Calculate time for the current iteration
//		t0 = Math.sqrt(2 * POSS[count] / (ACCSELERATION));
//
//		// Calculate v0 for the first phase of the iteration
//		setTime(POSS[count] / (getFPS() * (ACCSELERATION * t0)));
//	}
//
//	/**
//	 * Called when bouncing object reaches its top point.
//	 */
//	private void onTopPoint() {
//
//		// Reset bounce parameters
//		resetInnerVariables();
//
//		// Revert the object
//		setTime(POSS[count] / (getFPS() * (-getROC())));
//
//		super.execute();
//	}
//
//	/**
//	 * Called when bouncing object reaches its bottom point. New end point is
//	 * calculated. Time and speed is calculated
//	 */
//	public void onBottomPoint() {
//		// Calculate new end point for the next iteration
//		count++;
//		end = new Posn(start.getX(), start.getY() - POSS[count]);
//		// New time and speed
//		calculateTimeAndSpeed();
//		// Reset bounce parameters
//		resetInnerVariables();
//		// execute next iteration
//		super.execute();
//	}
//
//	/**
//	 * Update coordinates for the bouncing object
//	 * 
//	 * @return is the bouncing finished or not
//	 */
//	public void updateCoord() {
//		// Update distance to end point
//		current -= displacement;
//		// Set object on the corresponding place
//		RootPanel.get().setWidgetPosition(widget,
//				(int) Math.round((end.getX() + current * dx)),
//				(int) Math.round(end.getY() + current * dy));
//	}
//
//	/**
//	 * Updates the state
//	 */
//	public boolean update() {
//		// Increase time value
//		time++;
//		// If time is less then time limit
//		if (time < t0) {
//			// Process speed change according to the speed and acceleration
//			// directions
//			if (isFallingDown) {
//				setTime(POSS[count] / (getFPS() * (getROC() + ACCSELERATION)));
//			} else {
//				setTime(POSS[count] / (getFPS() * (getROC() - ACCSELERATION)));
//			}
//
//			// Update coordinates
//			updateCoord();
//			return true;
//		} else {
//			// This phase of iteration has finished.
//			finish();
//			return false;
//		}
//	}
//
//	/**
//	 * Called when current iteration phase has finished. According to the
//	 * current situation either initiate second phase of the iteration or new
//	 * iteration
//	 */
//	public void finish() {
//		// Stops current timer
//		timer.cancel();
//		// Place object into the final position
//		RootPanel.get().setWidgetPosition(widget, end.getX(), end.getY());
//
//		// Continue bundle
//
//		// Revert start and end points
//		Posn startStore = getStart();
//		setStart(end);
//		end = startStore;
//
//		// Change object moving direction
//		isFallingDown = !isFallingDown;
//
//		// If now the object is on the top point revert it to fall down
//		if (isFallingDown) {
//			onTopPoint();
//			return;
//		}
//
//		// Object is on the bottom top
//		// If there are still iteration planned initiate new iteration and
//		// revert the object to fly up
//		if (count != POSS.length - 1) {
//			// If it is not the last
//			onBottomPoint();
//			return;
//		}
//
//		// If not of the upper cases worked then the animation has to be finished
//		reset();
////		Main.getInstance().setFooterButtonsPlace();
//	}
//
//	/**
//	 * Reset everything. Called before starting new animation.
//	 * No need to call it before calling execute method.
//	 */
//	public void reset() {
//		count = 0;
//		end = new Posn(start.getX(), start.getY() - POSS[count]);
//		isFallingDown = false;
//		calculateTimeAndSpeed();
//		resetInnerVariables();
//
//	}
//
//	/**
//	 * Reset inner variables responsible for one iteration phase
//	 */
//	public void resetInnerVariables() {
//		current = start.distance(end);
//		target = 0;
//		time = 0;
//		angle = end.getAngle(start);
//		dx = Math.cos(angle);
//		dy = Math.sin(angle);
//	}
//
//	/** Resets start and end in relation to the current position */
//	public void refresh() {
//		// if(current == start.distance(end)){
//		int delta_x = widget.getAbsoluteLeft() - start.getX();
//		int delta_y = widget.getAbsoluteTop() - start.getY();
//		// reset start position
//		start.setX(widget.getAbsoluteLeft());
//		start.setY(widget.getAbsoluteTop());
//		// restart end position
//		end.setX(end.getX() + delta_x);
//		end.setY(end.getY() + delta_y);
//		// }
//	}
//
//	public void setTime(double time) {
//		displacement = (start.distance(end) / (frameduration * time));
//	}
//
//	public double getTime() {
//		return (Math.abs(start.distance(end) / (displacement * frameduration)));// total
//		// distance
//		// devided
//		// by
//		// number
//		// of
//		// frames
//	}
//
//	public boolean hasStarted() {
//		return !((current == start.distance(end)) || (current == 0));
//	}
//
//	public void setToggle() {
//		if (current == start.distance(end)) { // if the object is in it's
//			// starting position...
//			displacement = Math.abs(displacement);
//			target = 0;
//		} else { // otherwise...
//			displacement = -1 * displacement;
//			target = Math.abs(target - (start.distance(end)));
//		}
//	}
//
//	public String toString() {
//		return "Translate: (" + start.getX() + ", " + start.getY() + ") <--> ("
//				+ end.getX() + ", " + end.getY() + ")" + '\n'
//				+ "           displacement: " + displacement + " pixels/frame"
//				+ '\n' + "           frameduration: " + frameduration
//				+ " frames/second" + '\n' + "           angle: "
//				+ (angle / Math.PI) + "pi" + '\n' + "           target: "
//				+ target + '\n' + "           dx: " + dx + '\n'
//				+ "           dy: " + dy;
//	}
//}
