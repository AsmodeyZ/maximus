package forever.in.net.samplehttp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressLint("ClickableViewAccessibility")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class BubbleMenuActivity extends Activity {

    private GestureDetector gestureDetector;

    /*
	 * (non-Javadoc)
	 *
	 * @see android.support.v7.app.AppCompatActivity#onCreate(android.os.Bundle)
	 */

    double cur_x_Start;
    double cur_y_Start;
    double cur_x_Stop;
    double cur_y_Stop;
    private int global_move=0;
    private final List<TextAreaPoint> textAreaPoints= new ArrayList<>();

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView()
                .setSystemUiVisibility(mUIFlag);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        System.out.println("y: " + metrics.widthPixels + " x: "
                + metrics.heightPixels);
        int global_x = metrics.widthPixels;
        int global_y = metrics.heightPixels;

        double global_middle = 600;

        int[] BalColors = { Color.GREEN,
                Color.RED,
                Color.MAGENTA,
                Color.BLUE,
                0xDDffc966,
                0xDDf2e5f2,
                Color.CYAN,
                Color.YELLOW,
                0xDDccffff,
                0xDDAA0000,
                0xDD0382ef
        };

        Drawable ball_image = ContextCompat.getDrawable(getBaseContext(), R.drawable.ball_gray2);
        ball_image.mutate();

        //ball_image.setColorFilter(new  PorterDuffColorFilter(BalColors[0], PorterDuff.Mode.MULTIPLY));
        Drawable ball_image1 = ball_image.getConstantState().newDrawable();
        ball_image1.mutate();

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        // создание RelativeLayout
        // RelativeLayout linLayout = new RelativeLayout(this);
        MyRelView linLayout = new MyRelView(this, null, 0);
        // создаем LayoutParams
        RelativeLayout.LayoutParams linLayoutParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        // устанавливаем linLayout как корневой элемент экрана
        linLayout.setLayoutParams(linLayoutParam);
        linLayout.setBackground(ContextCompat.getDrawable(
                getApplicationContext(), R.drawable.ic_walpaper));
        setContentView(linLayout);


        int wx = linLayout.getWidthX();
        int wy = linLayout.getWidthY();
        System.out.println("Width - " + wx);
        System.out.println("Height - " + wy);

        linLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                if (gestureDetector.onTouchEvent(arg1)) {
                    System.out.println("Gesture");
                    return true;
                } else {
                    // your code for move and drag
                    System.out.println("Gesture");
                }

                return false;
            }
        });

        double a_el = global_x /4, b_el = global_y /4;
        double start_alpha = 0;
        int n_steps =9;
        int n_stable_per_point = 5;
        double stop_alpha = 2 * Math.PI;
        double alfa = Math.atan(global_y / global_x);
        //double alfa = 0;
        double step = stop_alpha / (n_steps * (n_stable_per_point+1));
        int shift_x = global_x /2;
        int shift_y = global_y /2;

        int radius = 150;

        //		Button[] buttonAll = new Button[n_steps];
        //		ImageView[] imageViewAll = new ImageView[n_steps];
        //		TextView[] textViewAll = new TextView[n_steps];

        List<EllipsePoint> list2 = generate_coord(a_el, b_el, start_alpha,
                stop_alpha, step, shift_x, shift_y, n_stable_per_point, radius,alfa);
        System.out.println(list2);
        Iterator<EllipsePoint> itr = list2.iterator();
        EllipsePoint tempEl,templElprev;

        int countButton = 0;String tempName;
        while (itr.hasNext()) {
            tempEl = itr.next();
            System.out.println(tempEl+"-"+tempEl.prev+"-"+tempEl.next);
            if (tempEl.stable) {
                ball_image1.setColorFilter(new  PorterDuffColorFilter(BalColors[countButton], PorterDuff.Mode.MULTIPLY));
                Drawable tempDraw = ball_image1.getConstantState().newDrawable();
                tempDraw.mutate();
                System.out.println(tempDraw);
                ball_image1.clearColorFilter();
                tempName = "blabla "+countButton+"\n"+countButton+" km";
				/*				buttonAll[countButton]=createBubbleButton(
						getApplicationContext(),tempDraw,
						tempName, 100+countButton, tempEl);*/
                //linLayout.addView(buttonAll[countButton]);


				/*imageViewAll[countButton] = createBubbleImage(
						getApplicationContext(), draw_but_res, "B"
								+ countButton,100+countButton, tempEl);
				  linLayout.addView(imageViewAll[countButton]);
				 */

                TextView tempTextView = createBubbleText(
                        getApplicationContext(), tempDraw, tempName, 100+countButton, tempEl);

                linLayout.addView(tempTextView);

                textAreaPoints.add(new TextAreaPoint(tempTextView,tempName,tempEl));

                countButton++;
                System.out.println(tempEl + " ; " + "x: " + tempEl.x + "; y: "
                        + tempEl.y + ";" + " Stable: " + tempEl.stable + ";"
                        + " Radius: " + tempEl.radius + ";"+ ";");
            }

        }


    }

    private static List<EllipsePoint> generate_coord(double a_el, double b_el,
                                                     double start_alpha, double stop_alpha, double step, int shift_x,
                                                     int shift_y, int n_stable_per_point, int init_radius, double alfa) {

        List<EllipsePoint> listGen = new LinkedList<>();

        double temp_x;
        double temp_y;

        int temp_x_i;
        int temp_y_i;
        int temp_x_i_alfa;
        int temp_y_i_alfa;
        double sin_alpha = Math.sin(alfa);
        double cos_alpha = Math.cos(alfa);
        int counter = 0;
        boolean stable_indic = false;
        boolean visible_indic = false;
        int radius;
        double additional_size=0;
        EllipsePoint firstEllipsePoint=null;
        EllipsePoint lastEllipsePoint=null;
        EllipsePoint newEllipsePoint=null;


        while (start_alpha <= stop_alpha) {
            if (counter == n_stable_per_point) {
                stable_indic = true;
                counter = 0;
            } else {
                stable_indic = false;
                counter++;
            }
            if (Math.cos(start_alpha)>=0){
                additional_size = init_radius*(Math.cos(start_alpha))/1.7;
                visible_indic =true;
            } else {
                additional_size = 0;
                visible_indic =false;
            }
            radius = (init_radius+(int)additional_size);
            temp_x = a_el * Math.cos(start_alpha);
            temp_y = b_el * Math.sin(start_alpha);


            temp_x_i_alfa = (int)(temp_x*cos_alpha+temp_y*sin_alpha);
            temp_y_i_alfa = (int)(-temp_x*sin_alpha+temp_y*cos_alpha);


            temp_x_i = temp_x_i_alfa +shift_x;
            temp_y_i = shift_y- temp_y_i_alfa;

            newEllipsePoint = new EllipsePoint(temp_x_i, temp_y_i, stable_indic,
                    radius,visible_indic);

            if (firstEllipsePoint==null){
                firstEllipsePoint = newEllipsePoint;
            }
            if (lastEllipsePoint!=null){
                lastEllipsePoint.next = newEllipsePoint;
            }
            newEllipsePoint.prev = lastEllipsePoint;
            lastEllipsePoint =newEllipsePoint;
            listGen.add(newEllipsePoint);
            start_alpha = start_alpha + step;
        }
        lastEllipsePoint.next = firstEllipsePoint;
        firstEllipsePoint.prev = lastEllipsePoint;
        return listGen;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static Button createBubbleButton(Context context,Drawable resouce_draw,
                                            String Name, int id, EllipsePoint tempEllipse) {
        Button tempbutton = new Button(context);
        //tempbutton.setEnabled(false);
        if (tempEllipse.visible){
            tempbutton.setText(Name);
        } else {
            tempbutton.setText("_");
        }
        tempbutton.setBackground(resouce_draw);
        tempbutton.setId(id);

        RelativeLayout.LayoutParams linnear_lay = new RelativeLayout.LayoutParams(
                tempEllipse.radius, tempEllipse.radius);
        // высота и ширина
        linnear_lay.leftMargin = tempEllipse.draw_x; // отступ слева
        linnear_lay.topMargin = tempEllipse.draw_y; // отступ сверху
        tempbutton.setLayoutParams(linnear_lay);

        return tempbutton;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static ImageView createBubbleImage(Context context,
                                              Drawable resouce_draw, String Name, int id, EllipsePoint tempEllipse) {
        ImageView tempImageView = new ImageView(context);

        // tempImageView.setText(Name);
        tempImageView.setBackground(resouce_draw);
        tempImageView.setId(id);

        RelativeLayout.LayoutParams linnear_lay = new RelativeLayout.LayoutParams(
                tempEllipse.radius, tempEllipse.radius);
        // высота и ширина
        linnear_lay.leftMargin = tempEllipse.draw_x; // отступ слева
        linnear_lay.topMargin = tempEllipse.draw_y; // отступ сверху
        tempImageView.setLayoutParams(linnear_lay);

        return tempImageView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static TextView createBubbleText(Context context,
                                             Drawable resouce_draw, String Name, int id, EllipsePoint tempEllipse) {
        TextView tempTextView = new TextView(context);

        // tempImageView.setText(Name);
        tempTextView.setBackground(resouce_draw);
        tempTextView.setId(id);
        tempTextView.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams linnear_lay = new RelativeLayout.LayoutParams(
                tempEllipse.radius, tempEllipse.radius);
        // высота и ширина
        linnear_lay.leftMargin = tempEllipse.draw_x; // отступ слева
        linnear_lay.topMargin = tempEllipse.draw_y; // отступ сверху
        tempTextView.setLayoutParams(linnear_lay);
        if (tempEllipse.visible){
            tempTextView.setText(Name);
        } else {
            tempTextView.setText("_");
        }
        tempTextView.setGravity(Gravity.CENTER);
        return tempTextView;
    }

    private class SingleTapConfirm extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        private static final int SWIPE_THRESHOLD_Y = 20;
        private static final int SWIPE_VELOCITY_THRESHOLD_Y = 20;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            onClick(e);
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {


            try {

                if ((e2.getX()+e2.getY())<300+720){
                    if (distanceY > 0) {
                        global_move--;

                    } else {
                        global_move++;

                    }
                } else {
                    if (distanceY < 0) {
                        global_move--;

                    } else {
                        global_move++;

                    }
                }
                int COUNT_FOR_ONE_STEP = 0;
                if (global_move < -COUNT_FOR_ONE_STEP) {
                    global_move=0;
                    onSwipeDown();
                } else if (global_move> COUNT_FOR_ONE_STEP){
                    global_move=0;
                    onSwipeUp();
                }

            } catch (Exception exception) {
            }
            return false;
        }

        // Determines the fling velocity and then fires the appropriate swipe event accordingly
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    private void onSwipeRight() {
        System.out.println("Move Rigth");

    }

    private void onSwipeLeft() {
        System.out.println("Move Left");

    }

    private void onSwipeUp() {
        System.out.println("Move UP");
        for (int i = 0; i < textAreaPoints.size(); i++) {
            textAreaPoints.get(i).move_prev(1);
            System.out.println(textAreaPoints.get(i).name);
        }
    }

    private void onSwipeDown() {
        System.out.println("Move DOWN");
        for (int i = 0; i < textAreaPoints.size(); i++) {
            textAreaPoints.get(i).move_next(1);
            textAreaPoints.get(i).textView.invalidate();
            System.out.println(textAreaPoints.get(i).name);
        }
    }

    private void onClick(MotionEvent e) {

        for (int i = 0; i < textAreaPoints.size(); i++) {
            if (textAreaPoints.get(i).point.visible){
                double distance=0;
                distance=(textAreaPoints.get(i).point.x-e.getX())*(textAreaPoints.get(i).point.x-e.getX());
                distance=distance+(textAreaPoints.get(i).point.y-e.getY())*(textAreaPoints.get(i).point.y-e.getY());
                distance=textAreaPoints.get(i).point.radius*textAreaPoints.get(i).point.radius/4 -distance;
                if (distance>0){
                    textAreaPoints.get(i).is_clicked();
                    textAreaPoints.get(i).textView.invalidate();
                    System.out.println(textAreaPoints.get(i).name);
                    break;
                }
            }
        }

    }

    private void onDoubleClick() {

    }

    private void onLongClick() {

    }


}