package me.khrystal.verticaltextviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.khrystal.widget.VerticalTextView;

public class MainActivity extends AppCompatActivity {

    VerticalTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (VerticalTextView) findViewById(R.id.text);
        textView.setText("强队要争冠，但莱切斯特城赢球；弱队要保级，但莱切斯特城赢球；赔率不看好，但莱切斯特城赢球；早场大热场，但莱切斯特城赢球；不管你是谁，莱切斯特城都赢球。\n" +
                "　　曼城球迷移情别恋狐狸精，曼联领先竟遭惊天大逆转。KICK AND WIN到底是一句口号还是一个幌子,\\n 英超莱切斯特城疯狂赢球的背后，到底隐藏怎样不为人知、惊世骇俗、神秘离奇的真相？\n" +
                "　　请看今日为您播出的《探索·发现》特别节目——“足球小能手懂个球之信仰与足球的完美合体”");

    }
}
