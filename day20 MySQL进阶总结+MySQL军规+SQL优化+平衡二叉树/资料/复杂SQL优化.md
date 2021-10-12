# 场景一：

```xml
 <select id="getProjectByVersionProject" resultType="com.qf.entity.dto.ProjectVersionDto">
        select dpp.guid projectGuid,
        dpp.package_number packageNumber,
        dpp.name projectName,
        dpp.app_version_no versionNo,
        dpp.creation_time createTime
        FROM qf_device_package_project dpp left JOIN (
        SELECT manager_id, PROJECT_GUID FROM csm_manager_custom_project
        UNION
        SELECT manager_id, PROJECT_GUID FROM csm_manager_region_project_rela
        ) dp on dp.PROJECT_GUID = dpp.guid
        WHERE dpp.state = 0 and dp.manager_id = #{query.managerId}
        <if test="query.projectName !=null and query.projectName !=''">
            and dpp.name like CONCAT('%',#{query.projectName},'%')
        </if>
        <if test="query.versionNo != null and query.versionNo !=''">
            and dpp.app_version_no = #{query.versionNo}
        </if>
        <if test="pagination.beginIndex !=null">
            limit #{pagination.beginIndex},#{pagination.length}
        </if>
    </select>
```



# 场景二： countdownlatch

业务层面拆分in 1000条 拆100个 10条。==多线程做了100次查询，需要合并查询结果，用countdownlatch来保证每个线程都执行完毕。再把合并后的结果输出。参考：https://blog.csdn.net/CiZhiYiHeng_/article/details/111826836

```xml
<select id="selectAfterSaleManagementsByIds" resultType="com.qf.entity.AfterSaleManagement">
        SELECT
        uasm.ID, DEVICE_KEY, PHOTO_URL, STATE, COMPANY_NAME,NAME, PHONE,uc.CONTENT as PROBLEM_TYPE_GUID,
        VERSION_NO,
        PROBLEM, SHIPPING_METHOD,EXPRESS_NAME,EXPRESS_NUMBER, REPAIR_PERSONNEL, REPAIR_LOG, FOLLOW_UP_FEEDBACK,
        REMARKS,REFUSAL,PRODUCT_MODEL,MEASUREMENT_PROBLEM,BIT_NUMBER,RECEIPT_DATE,
        uasm.CREATE_TIME,uasm.MODIFY_TIME
        FROM
        qf_after_sale_management uasm LEFT JOIN
        qf_constant uc
        on uasm.PROBLEM_TYPE_GUID = uc.GUID
        WHERE uasm.ID IN
        <foreach item="afterSaleManagementId" index="index" collection="afterSaleManagementIds" open="("
                 separator="," close=")">
            #{afterSaleManagementId}
        </foreach>
    </select>
```



# 场景三

```xml
<select id="queryPageList" resultType="com.qf.entity.DeviceApprove">
        select da.id,
        da.device_key,
        da.create_time,
        da.approve_time,
        da.extend_json,
        da.reject_reason,
        da.approve_status,
        da.manager_id,
        ed.device_type,
        ed.imei,
        ed.device_status,
        ed.device_model,
        ed.device_info
        from qf_device_approve da
        left join qf_external_device ed on da.device_key = ed.device_key
        where 1 =1
        <if test="searchModel.id != null">
            da.id = #{searchModel.id,jdbcType=BIGINT}
        </if>
        <if test="searchModel.deviceKey != null">
            and da.device_key = #{searchModel.deviceKey,jdbcType=VARCHAR}
        </if>
        <if test="searchModel.deviceStatus != null">
            and ed.device_status = #{searchModel.deviceStatus,jdbcType=TINYINT}
        </if>
        <if test="searchModel.deviceType != null">
            and ed.device_type = #{searchModel.deviceType,jdbcType=TIMESTAMP}
        </if>
        <if test="searchModel.approveStatus != null">
            and da.approve_status = #{searchModel.approveStatus,jdbcType=TINYINT}
        </if>
        <if test="searchModel.managerId != null">
            and da.manager_id = #{searchModel.managerId,jdbcType=BIGINT}
        </if>
        <if test="searchModel.startTime != null">
            and da.approve_time &gt;= #{searchModel.startTime,jdbcType=TIMESTAMP}
        </if>
        <if test="searchModel.endTime != null">
            and da.approve_time &lt;= #{searchModel.endTime,jdbcType=TIMESTAMP}
        </if>
        and da.approve_status <![CDATA[ <> ]]> 0
        and da.is_delete=0
        and ed.is_delete = 0
        order by da.create_time desc
        limit #{pagination.beginIndex},#{pagination.length}
    </select>
```



# 场景四

```xml
<select id="listDevicePackageByTypeAndPlatform" resultType="com.qf.entity.DevicePackage">
        SELECT <include refid="Base_Column_List" />
        apk.STATE apkState,
        system.STATE systemState,
        al.STATE algorithmState,
        apk.VERSION_NO apkVersion,
        system.VERSION_NO systemVersion,
        al.VERSION_NO algorithmVersion,
        apk.DISABLE_REASON apkDisableReason,
        system.DISABLE_REASON systemDisableReason,
        al.DISABLE_REASON algorithmDisableReason,
        dp.APK_GUID,
        dp.ALGORITHM_GUID,
        dp.SYSTEM_GUID,
        dp.VERSION_SETTING
        FROM qf_device_package dp
        LEFT JOIN qf_apk apk on dp.APK_GUID = apk.GUID
        LEFT JOIN qf_system system on dp.SYSTEM_GUID = system.GUID
        LEFT JOIN qf_algorithm al on dp.ALGORITHM_GUID = al.GUID
        WHERE dp.TYPE = #{type} AND dp.PLATFORM = #{platform}
    </select>
```



# 场景五

```xml
<select id="listDevicePackage" resultType="com.qf.entity.DevicePackage">
        SELECT <include refid="Base_Column_List" />
        apk.STATE apkState,
        system.STATE systemState,
        al.STATE algorithmState,
        apk.VERSION_NO apkVersion,
        system.VERSION_NO systemVersion,
        al.VERSION_NO algorithmVersion,
        apk.DISABLE_REASON apkDisableReason,
        system.DISABLE_REASON systemDisableReason,
        al.DISABLE_REASON algorithmDisableReason,
        dp.APK_GUID,
        dp.ALGORITHM_GUID,
        dp.SYSTEM_GUID,
        dp.VERSION_SETTING
        FROM qf_device_package dp
        LEFT JOIN qf_apk apk on dp.APK_GUID = apk.GUID
        LEFT JOIN qf_system system on dp.SYSTEM_GUID = system.GUID
        LEFT JOIN qf_algorithm al on dp.ALGORITHM_GUID = al.GUID
        <where>
            1=1
            AND dp.STATE = 0
            <if test="devicePackage.id != null and devicePackage.id != 0 ">
                and dp.ID like '%${devicePackage.id}%'
            </if>
            <if test="devicePackage.versionSetting != null ">
                and dp.VERSION_SETTING = #{devicePackage.versionSetting}
            </if>
            <if test="devicePackage.description != null and devicePackage.description != '' ">
                and dp.DESCRIPTION like '%${devicePackage.description}%'
            </if>
            <if test="devicePackage.platform != null and devicePackage.platform != 0 ">
                and dp.PLATFORM = #{devicePackage.platform}
            </if>
            <if test="devicePackage.type != null and devicePackage.type != 0">
                and dp.type = #{devicePackage.type}
            </if>
            <if test="devicePackage.hardwareVersion != null and devicePackage.hardwareVersion != '' ">
                and dp.HARDWARE_VERSION = #{devicePackage.hardwareVersion}
            </if>
            <if test="devicePackage.stateReview != null and devicePackage.stateReview != 0 ">
                and dp.STATE_REVIEW = #{devicePackage.stateReview}
            </if>
            <if test="devicePackage.projectGuid != null and devicePackage.projectGuid != ''  ">
                and dp.PROJECT_GUID = #{devicePackage.projectGuid}
            </if>
            <if test="devicePackage.guid != null and devicePackage.guid != '' ">
                and dp.GUID = #{devicePackage.guid}
            </if>
            <if test="devicePackage.versionNo != null and devicePackage.versionNo != '' ">
                and dp.VERSION_NO like "%${devicePackage.versionNo}%"
            </if>
            <if test="devicePackage.apkVersion != null and devicePackage.apkVersion != '' ">
                and apk.VERSION_NO like "%${devicePackage.apkVersion}%"
            </if>
            <if test="devicePackage.algorithmVersion != null and devicePackage.algorithmVersion != '' ">
                and al.VERSION_NO like "%${devicePackage.algorithmVersion}%"
            </if>
            <if test="devicePackage.systemVersion != null and devicePackage.systemVersion != '' ">
                and system.VERSION_NO like "%${devicePackage.systemVersion}%"
            </if>
            and (dp.OTHER_INFO is null or  dp.OTHER_INFO != '测试使用')
        </where>
        ORDER BY dp.CREATION_TIME DESC ,dp.VERSION_NO DESC
        limit #{pagination.beginIndex},#{pagination.length}
    </select>
```



# 场景六

```xml
<select id="queryByPackageName" parameterType="java.lang.String" resultType="com.qf.entity.DevicePackage">
        SELECT <include refid="Base_Column_List" />
        apk.STATE apkState,
        system.STATE systemState,
        al.STATE algorithmState,
        apk.VERSION_NO apkVersion,
        system.VERSION_NO systemVersion,
        al.VERSION_NO algorithmVersion,
        apk.DISABLE_REASON apkDisableReason,
        system.DISABLE_REASON systemDisableReason,
        al.DISABLE_REASON algorithmDisableReason,
        dp.APK_GUID,
        dp.ALGORITHM_GUID,
        dp.SYSTEM_GUID,
        dp.VERSION_SETTING
        FROM qf_device_package dp
        LEFT JOIN qf_apk apk on dp.APK_GUID = apk.GUID
        LEFT JOIN qf_system system on dp.SYSTEM_GUID = system.GUID
        LEFT JOIN qf_algorithm al on dp.ALGORITHM_GUID = al.GUID
        <where>
            1=1
            AND dp.STATE = 0
            and dp.DESCRIPTION like '%${description}%'
            and (dp.OTHER_INFO is null or  dp.OTHER_INFO != '内部测试使用')
        </where>
        ORDER BY dp.VERSION_NO DESC
    </select>

```



# 场景七

```xml
 <select id="getSystemAllByQuery" resultType="com.qf.entity.firmware.System">
        SELECT
        GUID,
        us.NAME,
        HARDWARE_VERSION,
        VERSION_NO,
        DESCRIPTION,
        MANAGER_ID,
        us.CREATE_TIME,
        STATE,
        FEATURE_NEW,
        CONTENT_FIX,
        OTHER_INFO,
        ma.NAME managerName,
        DISABLE_REASON disableReason,
        (select count(1) FROM qf_device_package WHERE SYSTEM_GUID = us.GUID AND STATE = 0) packageNum
        FROM qf_system us
        LEFT JOIN qf_manager ma ON ma.ID = us.MANAGER_ID
        <where>
            <if test="systemSearchModel.guid!=null">
                us.GUID = #{systemSearchModel.guid}
            </if>
            <if test="systemSearchModel.hardwareVersion!=null">
                us.HARDWARE_VERSION = #{systemSearchModel.hardwareVersion}
            </if>
            <if test="systemSearchModel.name !=null and systemSearchModel.name != ''">
                AND (us.VERSION_NO like "%${systemSearchModel.name}%" OR us.NAME like "%${systemSearchModel.name}%")
            </if>
            <if test="systemSearchModel.state != null">
                AND us.STATE = #{systemSearchModel.state}
            </if>
            <if test="systemSearchModel.endDate != null">
                AND us.CREATE_TIME &lt; #{systemSearchModel.endDate}
            </if>
            <if test="systemSearchModel.startDate != null">
                AND us.CREATE_TIME &gt; #{systemSearchModel.startDate}
            </if>
        </where>
        ORDER BY us.CREATE_TIME DESC
    </select>
```



# 场景八

```xml
<select id="selectDeviceByUserGuidAndDeviceKey" resultType="com.qf.entity.GroundDevice">
        select ud.NAME, ud.DEVICE_KEY, ud.APP_ID, ud.CLIENT_ID, ud.USER_GUID, ud.CID,
            (
                select qf_application.NAME from qf_ground_service.qf_application
                where
                    <if test="userGuid != null">
                        USER_GUID = #{userGuid} AND
                    </if>
                    qf_application.APP_ID = ud.APP_ID
            ) APP_NAME,
            ds.VERSION_NO
        from qf_device ud
        left join qf_device_service.qf_device ds on ds.DEVICE_KEY = ud.DEVICE_KEY
        <where>
            <if test="userGuid != null">
                ud.APP_ID IN (
                select APP_ID from qf_ground_service.qf_application
                where USER_GUID = #{userGuid}
                ) AND
            </if>
            ud.DEVICE_KEY = #{deviceKey}
            <if test="appId != null and appId != '' ">and ud.APP_ID = #{appId}</if>
        </where>
    </select>
```

