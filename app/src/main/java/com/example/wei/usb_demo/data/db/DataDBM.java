package com.example.wei.usb_demo.data.db;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.example.wei.usb_demo.data.db.bean.BloodOxygenModel;
import com.example.wei.usb_demo.data.db.bean.ModelBloodPressure;
import com.example.wei.usb_demo.data.db.bean.ModelBloodSugar;
import com.example.wei.usb_demo.utils.file.EcgDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenqiang on 2017/2/10.
 */

/**
 * 血糖，血压，血氧等数据库管理类
 */
public class DataDBM {
    private static final String TAG = DataDBM.class.getName();
    public static final String SQL_AND = "=? AND ";
    public static final int ONE_THOUSAND = 1000;
    private static DataDBM sSingleton = null;
    private Context context;
    private static final int QUERY_COUNT = 500;

    private DataDBM(Context context) {
        this.context = context;
    }

    public static DataDBM getInstance(Context context) {
        synchronized (DataDBM.class) {
            if (sSingleton == null) {
                if (context != null) {
                    sSingleton = new DataDBM(context.getApplicationContext());
                }
            }
        }
        return sSingleton;
    }

    public long insertModelBloodPressure(ModelBloodPressure modelBloodPressure) {
        if (modelBloodPressure != null) {
            if (modelBloodPressure.getId() == 0) {
                Uri uri = context.getContentResolver().insert(Authorities.DataPressure.AUTHORITY_URI, modelBloodPressure.getValues());
                if (uri != null) {
                    try {
                        return ContentUris.parseId(uri);
                    } catch (Exception e) {
                    }
                }
            }
        }

        return 0;
    }

    public ArrayList<ModelBloodPressure> getAllModelBloodPressure() {
        ArrayList<ModelBloodPressure> modelBloodPressures = new ArrayList<ModelBloodPressure>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Authorities.DataPressure.AUTHORITY_URI, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                ModelBloodPressure modelBloodPressure = ModelBloodPressure.getFromCusor(cursor);
                if (modelBloodPressure != null) {
                    modelBloodPressures.add(modelBloodPressure);
                }
            }
            return modelBloodPressures;
        } catch (Exception e) {
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    public long insertModelBloodSugar(ModelBloodSugar modelBloodSugar) {
        if (modelBloodSugar != null) {
            if (modelBloodSugar.getId() == 0) {
                Uri uri = context.getContentResolver().insert(Authorities.DataSugar.AUTHORITY_URI, modelBloodSugar.getValues());
                if (uri != null) {
                    try {
                        return ContentUris.parseId(uri);
                    } catch (Exception e) {
                    }
                }
            }
        }

        return 0;
    }

    public List<ModelBloodSugar> getAllModelBloodSugara() {
        ArrayList<ModelBloodSugar> modelBloodSugars = new ArrayList<ModelBloodSugar>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Authorities.DataSugar.AUTHORITY_URI, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                ModelBloodSugar modelBloodSugar = ModelBloodSugar.getFromCusor(cursor);
                if (modelBloodSugar != null) {
                    modelBloodSugars.add(modelBloodSugar);
                }
            }
            return modelBloodSugars;
        } catch (Exception e) {
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    public long insertModelBloodOxygen(BloodOxygenModel modelBloodOxygen) {
        if (modelBloodOxygen != null) {
            if (modelBloodOxygen.getId() == 0) {
                Uri uri = context.getContentResolver().insert(Authorities.DataSpo2h.AUTHORITY_URI, modelBloodOxygen.getValues());
                if (uri != null) {
                    try {
                        return ContentUris.parseId(uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return 0;
    }

    public List<BloodOxygenModel> getAllBloodOxygenModels() {
        ArrayList<BloodOxygenModel> modelBloodOxygens = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Authorities.DataSpo2h.AUTHORITY_URI, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                BloodOxygenModel modelBloodOxygen = BloodOxygenModel.getFromCusor(cursor);
                if (modelBloodOxygen != null) {
                    modelBloodOxygens.add(modelBloodOxygen);
                }
            }
            return modelBloodOxygens;
        } catch (Exception e) {
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    private StringBuilder genSuggestQuery1(long startTime, long endTime) {
        StringBuilder where = new StringBuilder();
        where.append(ModelBloodSugar.Columns.UID);
        where.append(SQL_AND);
        where.append(ModelBloodSugar.Columns.DELETED);
        where.append(" = 0 AND ");
        where.append(ModelBloodSugar.Columns.COLUMNS_DATA_TIME);
        where.append(" >= ");
        where.append(startTime / ONE_THOUSAND);
        where.append(" AND ");
        where.append(ModelBloodSugar.Columns.COLUMNS_DATA_TIME);
        where.append(" <= ");
        where.append(endTime / ONE_THOUSAND);
        where.append(" AND ");
        where.append(ModelBloodSugar.Columns.COLUMNS_VALUE);
        where.append(" >= 1.1 AND ");
        where.append(ModelBloodSugar.Columns.COLUMNS_VALUE);
        where.append(" <= 33.4 ");

        return where;
    }

    /**
     * 查询用户某段时间的血糖数据
     *
     * @param sn
     * @param startTime
     * @param endTime
     * @return
     */
    public ArrayList<ModelBloodSugar> getBloodSugarDataByDate(String sn, long startTime, long endTime) {

        ArrayList<ModelBloodSugar> list = new ArrayList<ModelBloodSugar>();
        if (TextUtils.isEmpty(sn)) {
            return list;
        }
        StringBuilder where = genSuggestQuery1(startTime, endTime);
        String[] args = new String[]{sn};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Authorities.DataPressure.AUTHORITY_URI, null, where.toString(), args, null);
            while (cursor != null && cursor.moveToNext()) {
                ModelBloodSugar data = new ModelBloodSugar();
                data.getValuesFromCursor(cursor);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;

    }
  
    public long insertEcgDataSource(EcgDataSource ecgDataSource) {
        if (ecgDataSource != null) {
            if (ecgDataSource.getId() == 0) {
                Uri uri = context.getContentResolver().insert(Authorities.DataEcg.AUTHORITY_URI, ecgDataSource.getValues());
                if (uri != null) {
                    try {
                        return ContentUris.parseId(uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return 0;
    }

    public List<EcgDataSource> getAllEcgDataSources() {
        ArrayList<EcgDataSource> ecgDataSourcesArr = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Authorities.DataEcg.AUTHORITY_URI, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                EcgDataSource ecgDataSource = EcgDataSource.getFromCusor(cursor);
                if (ecgDataSource != null) {
                    ecgDataSourcesArr.add(ecgDataSource);
                }
            }
            return ecgDataSourcesArr;
        } catch (Exception e) {
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }
}
