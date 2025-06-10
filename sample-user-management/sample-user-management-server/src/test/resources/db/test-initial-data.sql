INSERT INTO sampl_usr_mgmt_usr (id, login, name, email, system_admin)
    VALUES (2, 'felipedesiderati', 'Felipe Desiderati', 'felipedesiderati@springbloom.dev', false);

INSERT INTO sampl_usr_mgmt_grp (id, name, type, id_parent, order_as_division, order_as_workgroup)
    VALUES (1, 'Division A', 'D', null, null, null);

INSERT INTO sampl_usr_mgmt_grp (id, name, type, id_parent, order_as_division, order_as_workgroup)
    VALUES (2, 'Division A.1', 'D', 1, 0, null);

INSERT INTO sampl_usr_mgmt_grp (id, name, type, id_parent, order_as_division, order_as_workgroup)
    VALUES (3, 'Division A.1.1', 'D', 2, 0, null);

INSERT INTO sampl_usr_mgmt_grp (id, name, type, id_parent, order_as_division, order_as_workgroup)
    VALUES (4, 'Workgroup B', 'W', null, null, 0);

INSERT INTO sampl_usr_mgmt_role (id, name) VALUES (1, 'GROUP ADMINISTRATOR');
INSERT INTO sampl_usr_mgmt_role (id, name) VALUES (2, 'GROUP USER');
INSERT INTO sampl_usr_mgmt_role (id, name) VALUES (3, 'GROUP GUEST');

INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (1, 1, 1);
INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (3, 1, 2);
INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (4, 1, 3);
INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (5, 1, 4);

INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (6, 2, 1);
INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (7, 2, 2);
INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (8, 2, 3);
INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (9, 2, 4);

INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (10, 3, 1);
INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (11, 3, 2);
INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (12, 3, 3);
INSERT INTO sampl_usr_mgmt_alloc (id, id_role, id_group) VALUES (13, 3, 4);

INSERT INTO sampl_usr_mgmt_assign (id_user, id_allocation) VALUES (2, 5);
INSERT INTO sampl_usr_mgmt_assign (id_user, id_allocation) VALUES (2, 7);
INSERT INTO sampl_usr_mgmt_assign (id_user, id_allocation) VALUES (2, 8);
INSERT INTO sampl_usr_mgmt_assign (id_user, id_allocation) VALUES (2, 10);
