import Link from "next/link";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import {Menu} from "antd";

const map = new Map()

const rootSubmenuKeys = ['user', 'role', 'resource', 'verification'];

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
    getItem(<Link href="/main/user">User</Link>, 'user'),
    getItem(<Link href="/main/resource">Resource</Link>, 'resource'),
    getItem(<Link href="/main/role">Role</Link>, 'role'),
    getItem(<Link href="/main/verification">Request Verification</Link>, 'verification'),
];

export default function MainSider() {
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
        <>
            <Menu
                openKeys={openKeys}
                selectedKeys={selectedKeys}
                onOpenChange={onOpenChange}
                mode="inline"
                items={items}
                style={{height: "100%"}}
            />
        </>
    )
}