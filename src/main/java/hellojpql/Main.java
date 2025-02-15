package hellojpql;

import jakarta.persistence.*;

import java.util.Collection;

import static hellojpql.MemberType.ADMIN;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("team1");
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자");
            member1.setAge(10);
            member1.setTeam(team);
            member1.setType(ADMIN);

            Member member2 = new Member();
            member2.setUsername("관리자");
            member2.setAge(10);
            member2.setTeam(team);
            member2.setType(ADMIN);

            em.persist(member1);
            em.persist(member2);

            em.flush();
            em.clear();
            //사용자 정의 함수
//            String query = "select function('group_concat', m.username) from Member m";

            //컬렉션 값 연관 관계: 묵시적 내부 조인 발생 & 탐색X (더 이상 필드를 추가하지 못함)
            //묵시적 조인
            String query = "select m From Team t join t.members m";
            em.createQuery(query);

            Collection result = em.createQuery(query, Collection.class)
                    .getResultList();

            System.out.println("s = " + result);
            //반환 타입이 명확할 때
//            TypedQuery<Member> selectMFromMemberM = em.createQuery("select m from Member m", Member.class);
//            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
//                    .setParameter("username", "kim")
//                    .getSingleResult();
//
//            System.out.println("result.getUsername() = " + result.getUsername());

            //결과가 반드시 하나 일 때에만 사용 (없거나 둘 이상일 때 Exception 발생함)
//            String singleResult = query.getSingleResult();

            //반환 타입이 명확하지 않을 때
//            Query query2 = em.createQuery("select m.username, m.age from Member m");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
