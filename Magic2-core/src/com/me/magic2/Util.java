package com.me.magic2;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Util {

	public static String replace(String rawString, Object... params) {
		int c = 0;
		int count = 0;
		while (true) {
			String pat = String.format("{%d}", c);
			int index = rawString.indexOf(pat);
			if (index > -1) {
				count++;

			} else {
				break;
			}
			c++;
		}

		if (count != params.length) {
			throw new IllegalArgumentException(String.format(
					"invalid number of arguments: expected %d, but was %d",
					count, params.length));
		}

		for (int i = 0; i < count; i++) {
			rawString = rawString.replace(String.format("{%d}", i),
					params[i].toString());
		}

		return rawString;
	}

	public static void center(Actor actor) {
		Actor parent = actor.getParent();
		if (parent == null) {
			return;
		}

		float x = parent.getWidth() / 2 - actor.getWidth() / 2;
		float y = parent.getHeight() / 2 - actor.getHeight() / 2;
		actor.setPosition(x, y);
	}

}
