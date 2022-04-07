CREATE OR REPLACE VIEW public.xvcasuserprivileges
 AS
 SELECT up.keylong,
    up.keytext AS privilegekeytext,
    ug.keytext,
    upg.rowlevelsecurity
   FROM xtcasuserprivilege up
     JOIN xtcasluuserprivilegeusergroup upg ON up.keylong = upg.userprivilegekey AND upg.lastaction > 0
     JOIN xtcasusergroup ug ON ug.keylong = upg.usergroupkey AND ug.lastaction > 0
  WHERE up.lastaction > 0;
