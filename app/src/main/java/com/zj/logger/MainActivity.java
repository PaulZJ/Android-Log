package com.zj.logger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zj.loglib.internal.PatternLayout;
import com.zj.loglib.internal.appender.BasicAppender;
import com.zj.loglib.model.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Logger logger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test).setOnClickListener(this);
        BasicAppender appender = new BasicAppender(new PatternLayout("%m%n"));
        logger = new Logger();
        logger.addAppender(appender);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test:
                logger.trace("this ia a message");
                break;
        }
    }
}
