//package fi.tkk.media.xide.client.utils;
//
//
//	import com.MJC.client.Animations.CheckPointPath;
//	import com.MJC.client.Animations.Orbit;
//	import com.MJC.client.Animations.Translate;
//	import com.MJC.client.Core.Posn;
//	import com.MJC.client.Effects.Blind;
//	import com.MJC.client.Effects.Fade;
//	import com.MJC.client.Effects.Slide;
//	import com.MJC.client.Effects.Blinds.BlindDown;
//	import com.MJC.client.Effects.Blinds.BlindLeft;
//	import com.MJC.client.Effects.Blinds.BlindRight;
//	import com.MJC.client.Effects.Blinds.BlindUp;
//	import com.MJC.client.Effects.Slides.SlideDown;
//	import com.MJC.client.Effects.Slides.SlideLeft;
//	import com.MJC.client.Effects.Slides.SlideRight;
//	import com.MJC.client.Effects.Slides.SlideUp;
//	import com.MJC.client.Sets.Sorted.ParallelSet;
//	import com.MJC.client.Sets.Unsorted.SeriesSet;
//	import com.google.gwt.core.client.EntryPoint;
//	import com.google.gwt.core.client.GWT;
//	import com.google.gwt.user.client.Window;
//	import com.google.gwt.user.client.ui.Button;
//	import com.google.gwt.user.client.ui.ClickListener;
//	import com.google.gwt.user.client.ui.DialogBox;
//	import com.google.gwt.user.client.ui.Image;
//	import com.google.gwt.user.client.ui.RootPanel;
//	import com.google.gwt.user.client.ui.Widget;
//
//	/**
//	 * Entry point classes define <code>onModuleLoad()</code>.
//	 */
//	public class animation implements EntryPoint {
//
//	  /**
//	   * This is the entry point method.
//	   */
//	  public void onModuleLoad() {
////		  ControlImageBundle panelimages = (ControlImageBundle) GWT.create(ControlImageBundle.class);
////		  MusicBundle musicbundle = (MusicBundle) GWT.create(MusicBundle.class);
//		  VideoBundle videobundle = (VideoBundle) GWT.create(VideoBundle.class);
////		  PreferencesBundle preferencesbundle = (PreferencesBundle) GWT.create(PreferencesBundle.class);
//		  
//		  
//		  
////		  Image music = panelimages.music_toolbar().createImage();
////		  Image music = new Image("images/music_toolbar_icon.png");
//		  
//		  Image music = musicbundle.music_toolbar().createImage();
////		  SimplePanel music_panel = new SimplePanel();
////		  music_panel.add(music);
////		  RootPanel.get().add(music_panel, 0, 0);
//		  
//		  
//		  Image preferences = preferencesbundle.preferences_toolbar().createImage();
////		  SimplePanel preferences_panel = new SimplePanel();
////		  preferences_panel.add(preferences);
////		  RootPanel.get().add(preferences_panel, 0, 200);
//		  
////		  Image video = new Image("images/video_toolbar_icon.png");
//		  Image video = videobundle.video_toolbar().createImage();
////		  SimplePanel video_panel = new SimplePanel();
////		  video_panel.add(video);
////		  RootPanel.get().add(video_panel, 200, 200);
//		  
//		  int fps = ((int) (.05625 * Window.getClientWidth()));
//		  if(fps > 80){
//			  fps = 80;
//		  }
//
//		  /////////////////////////DEMO////////////////////
//		  final Translate move = new Translate(music, new Posn(0, 0), new Posn(150, 250));
//		  move.setFPS(fps);
//		  DialogBox fps_readout = new DialogBox();
//		  fps_readout.setText("move fps: " + fps);
//		  RootPanel.get().add(fps_readout, 500, 0);
//		  
//		  Button move_btn = new Button("Toggle Move");
//		  RootPanel.get().add(move_btn, 370, 20);
//		  move_btn.setWidth("105px");
//		  move_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  move.refresh();
//				  move.toggle();
//			  }
//		  });
//
//		  final Orbit circle = new Orbit(video, new Posn(200, 200), 40, 0, 1, true);
//		  Button circle_btn = new Button("Toggle Circular Movement");
//		  RootPanel.get().add(circle_btn, 370, 70);
//		  circle_btn.setWidth("200px");
//		  circle_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  circle.refresh();
//				  circle.toggle();
//			  }
//		  });
//		  
//		  final CheckPointPath checkpoint = new CheckPointPath(preferences, new Posn(0, 200), new Posn(170, 0), new Posn(50, 100), new Posn(70, 150));
//		  Button checkpoint_btn = new Button("Toggle Checkpoint Movement");
//		  RootPanel.get().add(checkpoint_btn, 370, 120);
//		  checkpoint.setFPS(fps);
//		  checkpoint_btn.setWidth("220px");
//		  checkpoint_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  checkpoint.refresh();
//				  checkpoint.toggle();
//			  }
//		  });
//		  
//		  final Fade fade = new Fade(video);
//		  Button fade_btn = new Button("Toggle Fade");
//		  RootPanel.get().add(fade_btn, 370, 170);
//		  fade.setFPS(fps);
//		  fade_btn.setWidth("105px");
//		  fade_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  fade.refresh();
//				  fade.toggle();
//			  }
//		  });
//		  
//		  Button fadeOut_btn = new Button("Fade Out");
//		  RootPanel.get().add(fadeOut_btn, 480, 170);
//		  fadeOut_btn.setWidth("80px");
//		  fadeOut_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
//				  fade.fadeOut();
//			  }
//		  });
//		  
//		  Button fadeIn_btn = new Button("Fade In");
//		  RootPanel.get().add(fadeIn_btn, 565, 170);
//		  fadeIn_btn.setWidth("70px");
//		  fadeIn_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
//				  fade.fadeIn();
//			  }
//		  });
//		  
//		  final Slide slidedown = new Slide(video, new SlideDown());
//		  slidedown.refresh();
//		  Button slidedown_btn = new Button("Toggle Slide Down");
//		  RootPanel.get().add(slidedown_btn, 370, 220);
//		  slidedown.setFPS(fps);
//		  slidedown_btn.setWidth("150px");
//		  slidedown_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  slidedown.refresh();
//				  slidedown.toggle();
//			  }
//		  });
//		  
//		  final Slide slideup = new Slide(video, new SlideUp());
//		  Button slideup_btn = new Button("Toggle Slide Up");
//		  RootPanel.get().add(slideup_btn, 370, 270);
//		  slideup.setFPS(fps);
//		  slideup_btn.setWidth("130px");
//		  slideup_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  slideup.refresh();
//				  slideup.toggle();
//			  }
//		  });
//		  
//		  final Slide slideleft = new Slide(video, new SlideLeft());
//		  slideleft.setFPS(fps);
//		  Button slideleft_btn = new Button("Toggle Slide Left");
//		  RootPanel.get().add(slideleft_btn, 370, 320);
//		  slideleft_btn.setWidth("150px");
//		  slideleft_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  slideleft.refresh();
//				  slideleft.toggle();
//			  }
//		  });
//		  
//		  final Slide slideright = new Slide(video, new SlideRight());
//		  slideright.setFPS(fps);
//		  Button slideright_btn = new Button("Toggle Slide Right");
//		  RootPanel.get().add(slideright_btn, 370, 370);
//		  slideright_btn.setWidth("150px");
//		  slideright_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  slideright.refresh();
//				  slideright.toggle();
//			  }
//		  });
//		  
//		  final Blind blinddown = new Blind(video, new BlindDown());
//		  blinddown.setFPS(fps);
//		  Button blinddown_btn = new Button("Toggle Blind Down");
//		  RootPanel.get().add(blinddown_btn, 550, 220);
//		  blinddown_btn.setWidth("150px");
//		  blinddown_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  blinddown.refresh();
//				  blinddown.toggle();
//			  }
//		  });
//		  
//		  final Blind blindup = new Blind(video, new BlindUp());
//		  blindup.setFPS(fps);
//		  Button blindup_btn = new Button("Toggle Blind Up");
//		  RootPanel.get().add(blindup_btn, 550, 270);
//		  blindup_btn.setWidth("130px");
//		  blindup_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  blindup.refresh();
//				  blindup.toggle();
//			  }
//		  });
//		  
//		  final Blind blindleft = new Blind(video, new BlindLeft());
//		  blindleft.setFPS(fps);
//		  Button blindleft_btn = new Button("Toggle Blind Left");
//		  RootPanel.get().add(blindleft_btn, 550, 320);
//		  blindleft_btn.setWidth("150px");
//		  blindleft_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  blindleft.refresh();
//				  blindleft.toggle();
//			  }
//		  });
//		  
//		  final Blind blindright = new Blind(video, new BlindRight());
//		  blindright.setFPS(fps);
//		  Button blindright_btn = new Button("Toggle Blind Right");
//		  RootPanel.get().add(blindright_btn, 550, 370);
//		  blindright_btn.setWidth("150px");
//		  blindright_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  blindright.refresh();
//				  blindright.toggle();
//			  }
//		  });
//		  
//		  final SeriesSet sset = new SeriesSet(checkpoint, move, circle);
//		  sset.setFPS(fps);
//		  Button sset_btn = new Button("Toggle Series Set");
//		  RootPanel.get().add(sset_btn, 10, 300);
//		  sset_btn.setWidth("150px");
//		  sset_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
//				  sset.toggle();
//			  }
//		  });
//	 
//		  final ParallelSet pset = new ParallelSet(move, checkpoint, circle);
//		  pset.setFPS(fps);
//		  Button pset_btn = new Button("Toggle Parallel Set");
//		  RootPanel.get().add(pset_btn, 170, 300);
//		  pset_btn.setWidth("150px");
//		  pset_btn.addClickListener(new ClickListener(){
//			  public void onClick(Widget sender){
////				  pset.testSetIntegrity();
////				  System.out.println("");
//				  pset.toggle();
//			  }
//		  });
//		  
//	  }
//	}
