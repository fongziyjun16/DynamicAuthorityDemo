import {Menu} from "antd";
import {useEffect, useState} from "react";
import Link from "next/link";
import {useRouter} from "next/router";

const map = new Map()

const rootSubmenuKeys = ['user', 'role', 'authority', 'resource', 'verification'];

const getItem = (label, key, icon, children, type) => {
    if (typeof label !== "string") {
        const href = label.props.href
        map.set(href, [key, href.split("/")[2]])
    }
    return {
        key,
        icon,
        children,
        label,
        type,
    };
}

const items = [
    getItem('User', 'user', null, [
        getItem(<Link href="/main/user/view">View User</Link>, 'viewUser'),
        getItem(<Link href="/main/user/add">Add User</Link>, 'addUser'),
        getItem(<Link href="/main/user/role">Manage Role</Link>, 'userManageRole'),
        getItem(<Link href="/main/user/authority">Manage Authority</Link>, 'userManageAuthority'),
    ]),
    getItem('Role', 'role', null, [
        getItem(<Link href="/main/role/view">View Role</Link>, 'viewRole'),
        getItem(<Link href="/main/role/add">Add Role</Link>, 'addRole'),
        getItem(<Link href="/main/role/authority">Manage Authority</Link>, 'roleManageAuthority'),
    ]),
    getItem('Authority', 'authority', null, [
        getItem(<Link href="/main/authority/view">View Authority</Link>, 'viewAuthority'),
        getItem(<Link href="/main/authority/add">Add Authority</Link>, 'addAuthority'),
    ]),
    getItem('Resource', 'resource', null, [
        getItem(<Link href="/main/resource/view">View Resource</Link>, 'viewResource'),
        getItem(<Link href="/main/resource/role">Manage Role</Link>, 'resourceManageRoel'),
        getItem(<Link href="/main/resource/authority">Manage Authority</Link>, 'resourceManageAuthority'),
    ]),
    getItem(<Link href="/main/verification">Request Verification</Link>, 'verification'),
];

export default function AppNav() {

    const router = useRouter()
    const [selectedKeys, setSelectedKeys] = useState([])
    const [openKeys, setOpenKeys] = useState([])

    useEffect(() => {
        const openKeys = map.get(router.pathname)
        if (openKeys !== undefined) {
            // console.log(openKeys)
            setOpenKeys([...openKeys])
            setSelectedKeys([...openKeys])
        } else {
            setOpenKeys([])
        }
    }, [router])

    const onOpenChange = (keys) => {
        const latestOpenKey = keys.find((key) => openKeys.indexOf(key) === -1);
        if (rootSubmenuKeys.indexOf(latestOpenKey) === -1) {
            setOpenKeys(keys);
        } else {
            setOpenKeys(latestOpenKey ? [latestOpenKey] : []);
        }
    }

    return (
        <Menu
            openKeys={openKeys}
            selectedKeys={selectedKeys}
            onOpenChange={onOpenChange}
            mode="inline"
            items={items}
            style={{height: "100%"}}
        />
    )
}