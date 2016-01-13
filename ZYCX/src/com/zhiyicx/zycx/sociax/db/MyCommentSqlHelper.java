package com.zhiyicx.zycx.sociax.db;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import com.zhiyicx.zycx.sociax.exception.DataInvalidException;
import com.zhiyicx.zycx.sociax.exception.WeiboDataInvalidException;
import com.zhiyicx.zycx.sociax.modle.Comment;
import com.zhiyicx.zycx.sociax.modle.ListData;
import com.zhiyicx.zycx.sociax.modle.ReceiveComment;
import com.zhiyicx.zycx.sociax.modle.SociaxItem;
import com.zhiyicx.zycx.sociax.modle.User;
import com.zhiyicx.zycx.sociax.modle.Weibo;

public class MyCommentSqlHelper extends SqlHelper {
	private static MyCommentSqlHelper instance;
	private ThinksnsTableSqlHelper weiboTable;
	private ListData<SociaxItem> weiboDatas;

	private MyCommentSqlHelper(Context context) {
		this.weiboTable = new ThinksnsTableSqlHelper(context, DB_NAME, null, VERSION);
	}

	public static MyCommentSqlHelper getInstance(Context context) {
		if (instance == null) {
			instance = new MyCommentSqlHelper(context);
		}

		return instance;
	}

	private int transFrom(String str) {
		if (str.equals("WEB")) {
			return 0;
		} else if (str.endsWith("ANDROID")) {
			return 2;
		} else if (str.equals("IPHONE")) {
			return 3;
		} else if (str.equals("PHONE")) {
			return 1;
		}
		return 0;
	}

	private int transFavourt(boolean favorited) {
		if (favorited) {
			return 1;
		} else {
			return 0;
		}
	}

	private boolean isFavourt(int is) {
		if (is == 1) {
			return true;
		} else {
			return false;
		}
	}

	public long addComment(ReceiveComment comment) {
		ContentValues map = new ContentValues();
		map.put("uid", comment.getUid());
		map.put("userface", comment.getHeadUrl());
		map.put("timestamp", comment.getTimestemp());
		map.put("weiboId", comment.getWeiboId());
		map.put("commentId", comment.getCommentId());
		if (comment.getStatus() != null) {
			map.put("status", comment.getStatus().toJSON());
		}
		map.put("replyCommentId", comment.getReplyCommentId());
		map.put("replyUid", comment.getReplyUid());
		map.put("content", comment.getContent());
		map.put("uname", comment.getUname());
		map.put("commentUser", comment.getJsonUser());
		map.put("type", comment.getType().toString());
		map.put("replyComment", comment.getReplyJson());
		map.put("cTime", comment.getcTime());
		map.put("site_id", Thinksns.getMySite().getSite_id());
		map.put("my_uid", Thinksns.getMy().getUid());
		long l = weiboTable.getWritableDatabase().insert(ThinksnsTableSqlHelper.myCommentTable, null, map);
		return l;
	}

	/**
	 * 
	 * @param site_id
	 * @param myUid
	 * @return
	 */
	public ListData<SociaxItem> getDBCommentList() {
		if (Thinksns.getMy() == null) {
			return null;
		}
		Cursor cursor = weiboTable.getReadableDatabase().query(ThinksnsTableSqlHelper.myCommentTable, null,
				"site_id=" + Thinksns.getMySite().getSite_id() + " and my_uid=" + Thinksns.getMy().getUid(), null, null,
				null, "commentId DESC");
		weiboDatas = new ListData<SociaxItem>();
		if (cursor.moveToFirst()) {
			do {
				ReceiveComment comment = new ReceiveComment();
				comment.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
				comment.setHeadUrl(cursor.getString(cursor.getColumnIndex("userface")));
				comment.setCommentId(cursor.getInt(cursor.getColumnIndex("commentId")));
				comment.setContent(cursor.getString(cursor.getColumnIndex("content")));
				comment.setcTime(cursor.getString(cursor.getColumnIndex("cTime")));
				comment.setUname(cursor.getString(cursor.getColumnIndex("uname")));
				if (cursor.getString(cursor.getColumnIndex("replyComment")) != null) {
					try {
						comment.setReplyComment(
								new Comment(new JSONObject(cursor.getString(cursor.getColumnIndex("replyComment")))));
					} catch (DataInvalidException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				comment.setTimestemp(cursor.getInt(cursor.getColumnIndex("timestamp")));
				comment.setReplyCommentId(cursor.getInt(cursor.getColumnIndex("replyCommentId")));
				comment.setReplyUid(cursor.getInt(cursor.getColumnIndex("replyUid")));
				if (cursor.getString(cursor.getColumnIndex("status")) != null) {
					try {
						comment.setStatus(
								new Weibo(new JSONObject(cursor.getString(cursor.getColumnIndex("status"))), 1));
					} catch (WeiboDataInvalidException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (cursor.getString(cursor.getColumnIndex("commentUser")) != null) {
					try {
						comment.setUser(
								new User(new JSONObject(cursor.getString(cursor.getColumnIndex("commentUser")))));
					} catch (DataInvalidException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				comment.setWeiboId(cursor.getInt(cursor.getColumnIndex("weiboId")));
				comment.setType(cursor.getString(cursor.getColumnIndex("type")));

				cursor.moveToNext();
				weiboDatas.add(comment);

			} while (cursor.moveToNext());
			cursor.close();
		}
		return weiboDatas;
	}

	/**
	 * 获取DB评论的数量
	 * 
	 * @return
	 */
	public int getDBCommentSize() {
		Cursor cursor = weiboTable.getWritableDatabase()
				.rawQuery(
						"select count(*) from " + ThinksnsTableSqlHelper.myCommentTable + " where site_id = "
								+ Thinksns.getMySite().getSite_id() + " and my_uid = " + Thinksns.getMy().getUid(),
						null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			return 0;
		}
	}

	@Override
	public void close() {
		weiboTable.close();
	}

	// ///////***********************************////////////////////////////

	public boolean deleteWeibo(int count) {
		if (count > 19) {
			weiboTable.getWritableDatabase().execSQL("delete from my_comment where site_id = "
					+ Thinksns.getMySite().getSite_id() + " and my_uid = " + Thinksns.getMy().getUid());
		} else if (count > 0 && count < 20) {
			String sql = "delete from my_comment where commentId in (select weiboId from my_comment where site_id = "
					+ Thinksns.getMySite().getSite_id() + " and my_uid = " + Thinksns.getMy().getUid()
					+ " order by weiboId limit " + count + ")";
			weiboTable.getWritableDatabase().execSQL(sql);
		}
		return false;
	}

	/**
	 * 删除数据库缓存
	 */
	public void clearCacheDB() {
		weiboTable.getWritableDatabase().execSQL("delete from my_comment where site_id = "
				+ Thinksns.getMySite().getSite_id() + " and my_uid = " + Thinksns.getMy().getUid());
	}

}
