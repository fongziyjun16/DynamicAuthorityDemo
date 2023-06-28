import {Button, Col, Row} from "antd";
import {useRouter} from "next/router";
import Link from "next/link";

export default function AppHeader() {

    const router = useRouter()

    const signOut = () => {
        router.replace("/")
    }

    return (
        <Row>
            <Col span={12}>
                <Link href={"/main"}>
                    <span style={{color: "white"}}>
                        Dynamic Authority
                    </span>
                </Link>
            </Col>
            <Col span={12} style={{textAlign: "right"}}>
                <Button onClick={() => signOut()}>
                    Sign Out
                </Button>
            </Col>
        </Row>
    )
}